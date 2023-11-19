# Game Management Application

This repository contains a Java-based application designed to manage games within a game service provider environment.
The application handles the CRUD (Create, Read, Update, Delete) operations associated with games in a game service.
The application exposes REST endpoints to interact with the implemented GameService.
To efficiently store game data, an in-memory cache structure is implemented.

Game class is used to model game data within the application.
Consists fields:
name - unique value, required field for requests
newName - used for name updates, optional field for requests
creationDate
updateDate 
isActive - required field for requests

## How to build:

From the root app:

1. mvn clean install
2. windows: java -jar .\target\game-management-0.0.1-SNAPSHOT.jar 8080
   bash: java -jar ./target/game-management-0.0.1-SNAPSHOT.jar 8080

## Endpoints to test:

POST http://localhost:8080/games/
example JSON body:
```
{
"name": "Hercules",
"isActive": true
}
```

GET game by name http://localhost:8080/games/{name}

GET all games http://localhost:8080/games

PUT http://localhost:8080/games/
example JSON body:
```
{
"name": "Hercules",
"newName": "Hercules2",
"isActive": true
}
```

DELETE by name http://localhost:8080/games/{name}

## Tech stack:

- Java 8
- Spring Boot
- REST
- Maven

## Logs
Logs can be found in gameapplogs.log file under the root application directory.