package se.kry.codetest;

import io.vertx.core.json.JsonArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileDatabase implements Database {

    private Path file = Paths.get("/tmp/database");

    public FileDatabase() {
        if (! Files.exists(this.file)) {
            try {
                Files.createFile(this.file);
                write(new JsonArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();
        JsonArray jsonArray = read();
        for (int i = 0; i < jsonArray.size(); i++) {
            services.add(new Service(jsonArray.getJsonObject(i)));
        }
        return services;
    }

    @Override
    public void save(Service service) {
        List<Service> services = getAll();
        services.add(service);
        write(new JsonArray(services));
    }

    @Override
    public void delete(String id) {
        List<Service> services = getAll()
                .stream()
                .filter(service -> ! service.getId().equals(id))
                .collect(Collectors.toList());
        JsonArray jsonArray = new JsonArray(services);
        write(jsonArray);
    }

    @Override
    public void reset() {
        write(new JsonArray());
    }

    private void write(JsonArray jsonArray) {
        try {
            Files.write(this.file, jsonArray.encode().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonArray read() {
        String content = null;
        try {
            content = new String(Files.readAllBytes(this.file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonArray(content);
    }
}
