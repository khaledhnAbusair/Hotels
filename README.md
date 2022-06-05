# Available Hotels

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method
in the `com.maf.hotels.HotelsApplication` class from your IDE.

Alternatively you can use
the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html)
like so:

```shell
mvn spring-boot:run
```

## Spring Rest  with Swagger UI

Shows the list of Endpoints in the current RESTfUl webservice.

* [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Technical Details

* Using Feign client technology to simplifying HTTP API clients
* If any providers failed the other providers works correctly and return response
* Add retrying Feign calls to enhance retrying features If any providers was pressured or there was any problem

