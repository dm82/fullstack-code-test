# Service Poller

## Description

This is a gradle project that builds a simple service poller using Vertx.x (https://vertx.io/).
It keeps a list of services (defined by a URL), and periodically does a HTTP GET to each and saves the response ("OK" or "FAIL")

## Run the application

When the application is built, you can run the application directly from the command line:

    ./gradlew clean run

The application starts a server on `localhost:8080`.

## API

### Get all services

    curl http://localhost:8080/service

### Add a service

    curl -X POST http://localhost:8080/service -d '{"url": "https://kry.se"}'

### Delete a service

    curl -X DELETE http://localhost:8080/service/:id

## Available Databases

Three different databases are available:

- HashMapDatabase: an in memory store
- FileDatabase: a persistent store which uses a file behind the scenes
- MongoDatabase: a persistent store using MongoDB (incomplete, see comments in the MongoDatabase class)

To switch between databases, go to the MainVerticle class and uncomment the respective line, for example:

    private Database db = new FileDatabase();

### Notes about MongoDatabase

Before you can use the MongoDatabase backend, you need to run a dockerized version of MongoDB:

    docker run -d -p 27017:27017 mongo

## Tests

To run the tests:

    ./gradlew clean test
