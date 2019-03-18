package se.kry.codetest;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.sync.Sync;
import io.vertx.ext.sync.SyncVerticle;

import java.util.ArrayList;
import java.util.List;

public class MongoDatabase extends SyncVerticle implements Database {

    private MongoClient client;
    private String connection_string = "mongodb://localhost:27017";
    private String db_name = "kry";
    private String db_collection = "services";

    public MongoDatabase(){
        System.out.println("Starting Mongo");
        JsonObject mongoConfig = new JsonObject()
                .put("connection_string", connection_string)
                .put("db_name", db_name);
        client = MongoClient.createShared(Vertx.vertx(), mongoConfig);
    }

    // This function does not work due to the fact that Sync.awaitResult is called
    // from outside a "fiber". Not being familiar with Vert.x I decided to fall back
    // to a file database. It would be interesting to know how to make this to work.
    @Override
    public List<Service> getAll() {
        JsonObject query = new JsonObject();
        List<Service> services = new ArrayList<>();
        List<JsonObject> jsonObjects = Sync.awaitResult(h -> this.client.find(db_collection, query, h));
        for (JsonObject jsonObject : jsonObjects) {
            services.add(new Service(jsonObject));
        }
        return services;
    }

    @Override
    public void save(Service service) {
        JsonObject jsonObject = service.toJsonObject();
        jsonObject.put("_id", service.getId());
        this.client.save(this.db_collection, jsonObject, res -> {
            System.out.println("Saved url with id " + service.getId());
        });
    }

    @Override
    public void delete(String id) {
        JsonObject query = new JsonObject().put("_id", id);
        this.client.removeDocuments(this.db_collection, query, res -> {
            System.out.println("Removed url with id " + id);
        });
    }

    @Override
    public void reset() {
        // TODO
    }
}