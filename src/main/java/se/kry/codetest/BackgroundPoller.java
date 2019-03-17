package se.kry.codetest;

import java.util.List;
import java.util.Random;

public class BackgroundPoller {

  private Random random = new Random();

  public void pollServices(Database db) {
    List<Service> services = db.getAll();
    services.forEach(service -> service.setStatus(random.nextBoolean() ? "OK" : "FAIL"));
  }
}
