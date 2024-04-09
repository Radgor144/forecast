# Forecast App

## Table of Contents

- [General info](#Generalinfo)
- [Requirements](#Requirements)
- [Technologies](#Technologies)

## General info <a name = "Generalinfo"></a>

<a href="https://spring.io/projects/spring-boot" target="blank"> Spring Boot</a> application about forecast. The application provide user to get weather data. The application is connected to <a href="https://spring.io/projects/spring-boot" target="blank"> Weather API</a> to fetch weather data.

| Method |       URL        |             Description           |
| ------ | ---------------- | --------------------------------- |
| `Get`  | `/forecast/city` | Receives weather data for 15 days |

## Requirements <a name = "Requirements"></a>

- <a href="https://spring.io/projects/spring-boot" target="blank"> JDK 21 </a>
- <a href="https://spring.io/projects/spring-boot" target="blank"> Maven 3.x </a>

## How to run locally <a name = "How to run locally"></a>

1. Register account on https://weather.visualcrossing.com
2. Login to your account and copy api-key from: https://www.visualcrossing.com/account
![img.png](img.png)
3. Create application-local.yml with structure like:
   ``` yaml 
   weather:
      client:
        api-key: changeMe
   ```
4. Replace changeMe with api-key from 
5. Set profile to local in configuration
![img_1.png](img_1.png)

## Technologies <a name = "Technologies"></a>

### Project Created with:

- **Java 21** - The core programming language for building the project.
- **Spring boot** - It supports the rapid development of microservices-based applications and includes built-in features for configuration, dependency management, and application bootstrapping.

### Integration:

- **feign** - declarative web service client. Simplifies communication between services by enabling easy invocation of remote services from application code. It makes writing web service clients easier.

### Documentation:
- **swagger** - Auto-generate API documentation for better interactivity, comprehension and testing of API features.

### Tests:
- **JUnit** - Unit testing was implemented to check the correctness of individual code fragments in isolation, enabling faster error detection and facilitating refactoring.
- **Mockito** - It allows you to simulate the behaviour of real objects in a controlled test environment, making it easier to test code in isolation and verify interactions between objects.
- **wiremock** - WireMock enables the creation of mock HTTP servers that can be used to test applications that communicate using the HTTP protocol. It is used to create dummy servers that can pretend to be real services. With WireMock, it is possible to create pre-defined responses to specific HTTP requests.

### Build tools:
- **Maven** - Tool for managing dependencies, building, and managing projects.