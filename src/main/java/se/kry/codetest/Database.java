package se.kry.codetest;

import java.util.List;

public interface Database {
    List<Service> getAll();

    void save(Service service);

    void delete(String id);
}
