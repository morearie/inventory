http://localhost:8080/api/product/add-initial-# [ARIE][1] Inventory

This application was generated using Spring-Boot 2.5.5, you can find documentation and help at [https://spring.io/projects/spring-boot][2].

This is a "monolithic" application intended to be part of a microservice architecture.

This application is using Maven Project.

The stock problem might be due to concurrent update stock inventory product. We need to add version to block updating stock database.
in this solution, I created loking using Optimistic lock to prevent updatind data at the same time for each processes.
In Unit testing(see below how to run unit testing section), I created 3 test cases,
1. run normaly without concurrent
2. run process with concurrent
3. run process with concurrent and out of stock

## Prerequisites
* Install the version of [openjdk 11][3].
* You may need to set your `JAVA_HOME`.

## Project Lombok
This project is running with Project-Lombok
### Annotations used:
````
1. @Getter
2. @Setter
3. @EqualsAndHashCode(of = {"sentence", "val"})
4. @ToString(exclude = "val")
5. @Builder
6. @RequiredArgsConstructor, generates a constructor for all final fields, with parameter order same as field order
7. @NoArgsConstructor creates an empty constructor.
8. @AllArgsConstructor creates a constructor for all fields
````
### Setup for intellij

If you are using intellij, need to activate annotations processor:
    Settings -> Compiler -> Annotation Processors

Now install lombok plugin:

    Preferences -> Plugins
    Click Browse repositories...
    Search for "Lombok Plugin"
    Install
    Restart IntelliJ

### Setup for Eclipse(STS)

If we're using Eclipse IDE, we need to get the Lombok jar first. The latest version is located on Maven Central. For our example, we're using lombok-1.18.12.jar.
Next, we can run the jar via java -jar command and an installer UI will open. 
This tries to automatically detect all available Eclipse installations, but it's also possible to specify the location manually.
Once we've selected the installations, then we press the Install/Update button.

Now install lombok plugin:

    Help -> Eclipse Marketplace
    Search for "Lombok Plugin"
    Install
    Restart Eclipse(STS)

See the pom.xml file to see what line is doing what

## Development
To generate maven dependency in your development, simply run:
```
	./mvnw clean install -DskipTests
```

## To run unit testing
```
	./mvnw test
```

## To build application
```
	./mvnw clean install
```

## To run application
```
	./mvnw spring-boot:run
```

## H2 DATABASE
```
http://localhost:8080/h2
JDBC URL: jdbc:h2:file:./data/demodb
username: admin
password: admin
```

## SWAGGER UI (API docs)
please read on Swagger for more details for each API.
you may run postman runner to test concurrent for increase/decrease stock product

```
http://localhost:8080/swagger-ui.html
POST http://localhost:8080/api/product/add-initial-data to add initial data order and products
PUT http://localhost:8080/api/product/1/increase-stock/3 productId=1 and quantity to increase=3
PUT http://localhost:8080/api/product/1/decrease-stock/2 productId=1 and quantity to decrease=3
```


[1]: https://github.com/morearie/inventory
[2]: https://spring.io/projects/spring-boot
[3]: https://openjdk.java.net/projects/jdk/11/