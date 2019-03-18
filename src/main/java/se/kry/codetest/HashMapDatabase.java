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
    public void save(Service service) {
        services.put(service.getId(), service);
    }

    @Override
    public void delete(String id) {
        services.remove(id);
    }

    @Override
    public void reset() {
        this.services = new HashMap<>();
    }
}
