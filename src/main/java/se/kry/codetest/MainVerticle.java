package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.List;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {

  private Database db = new HashMapDatabase();
  private BackgroundPoller poller = new BackgroundPoller();

  @Override
  public void start(Future<Void> startFuture) {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
//    services.put("https://www.kry.se", "UNKNOWN");
//    vertx.setPeriodic(1000 * 60, timerId -> poller.pollServices(services));
    setRoutes(router);
    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(8080, result -> {
          if (result.succeeded()) {
            System.out.println("KRY code test service started");
            startFuture.complete();
          } else {
            startFuture.fail(result.cause());
          }
        });
  }

  private void setRoutes(Router router){
    router.route("/").handler(StaticHandler.create());
    router.route("/static/*").handler(StaticHandler.create());
    router.get("/service").handler(this::getServices);
    router.post("/service").handler(this::addService);
    router.delete("/service/:id").handler(this::removeService);
  }

  private void getServices(RoutingContext context) {
    System.out.println("Get all services");
    List<JsonObject> jsonServices = db.getAll()
            .stream()
            .map(service -> service.toJsonObject())
            .collect(Collectors.toList());
    context.response()
            .putHeader("content-type", "application/json")
            .end(new JsonArray(jsonServices).encode());
  }

  private void addService(RoutingContext context) {
    System.out.println("Add new service");
    JsonObject jsonBody = context.getBodyAsJson();
    String url = jsonBody.getString("url");
    if (Service.isValidURL(url)) {
      db.save(new Service(url));
      context.response()
              .setStatusCode(201)
              .putHeader("content-type", "text/plain")
              .end("OK");
    } else {
      context.response().setStatusCode(400).end();
    }
  }

  private void removeService(RoutingContext context) {
    System.out.println("Delete service");
    String id = context.request().getParam("id");
    if (id == null) {
      context.response().setStatusCode(400).end();
    } else {
      db.delete(id);
      context.response().setStatusCode(204).end();
    }
  }

}



