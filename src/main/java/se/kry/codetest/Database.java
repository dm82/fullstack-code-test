package se.kry.codetest;

import java.util.List;

public interface Database {
    List<Service> getAll();

    String save(String url);

    void delete(String id);
}
