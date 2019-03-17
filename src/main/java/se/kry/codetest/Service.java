package se.kry.codetest;

import io.vertx.core.json.JsonObject;

import java.util.UUID;
import org.apache.commons.validator.routines.UrlValidator;

public class Service {
    private String id;
    private String URL;
    private String status;

    public Service(String URL) {
        this.id = getUniqueIdentifier();
        this.URL = URL;
        this.status = "UNKNOWN";
    }

    private String getUniqueIdentifier() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Service fromJsonObject(JsonObject jsonObject) {
        Service service = new Service(jsonObject.getString("url"));
        service.status = jsonObject.getString("status");
        return service;
    }

    public JsonObject toJsonObject(){
        JsonObject json = new JsonObject();
        json.put("_id", this.getId());
        json.put("url", this.getURL());
        json.put("status", this.getStatus());
        return json;
    }

    public static boolean isValidURL(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
