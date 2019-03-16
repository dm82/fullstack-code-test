package se.kry.codetest;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HashMapDatabase implements Database {

    private HashMap<String, Service> services = new HashMap<>();

    @Override
    public List<Service> getAll() {
        return this.services
                .entrySet()
                .stream()
                .map(service ->
                        service.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public String save(String url) {
        Service service = new Service(url);
        String id = service.getId();
        services.put(id, service);
        return id;
    }

    @Override
    public void delete(String id) {
        services.remove(id);
    }
}
