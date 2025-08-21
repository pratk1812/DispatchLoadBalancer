# DispatchLoadBalancer
A Spring Boot–powered RESTful service that balances delivery orders across a fleet of vehicles using a spatial k-d tree and capacity-aware assignment algorithm. It ingests orders and vehicle registrations, computes optimized dispatch plans, and exposes endpoints to manage and retrieve data.

# Features
- Register and manage orders (latitude, longitude, priority, weight)
-	Register and manage vehicles (current location, capacity)
-	Nearest-neighbor search via a balanced k-d tree ()
-	Capacity-aware “best-fit” selection with slack vs. distance weighting
-	Automatic cleanup of processed orders and vehicles
- Detailed dispatch plan DTOs exposing:
  -	Vehicle ID
  -	Total distance traveled
  -	Total load assigned
  -	List of orders
 
# Tech Stack
- Java 17
- Spring Boot 3.x
- H2 database
- Spring Data JPA (H2 / PostgreSQL)
- MapStruct for entity⇄DTO mapping
- Apache Log4j 2
- Maven

# Getting Started

## Prerequisites
- Java 17 SDK
- Maven 3.6+
- (Optional) PostgreSQL or H2 for persistence

## Installation

```
git clone https://github.com/pratk1812/DispatchLoadBalancer.git
cd DispatchLoadBalancer
```

## Configuration
Edit src/main/resources/application.properties to configure your database:

```
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

## Running the Application

### Build

```
mvn clean package
```

### Run

```
java -jar target/dispatch-load-balancer-0.0.1-SNAPSHOT.jar
```

### Or via Maven

```
mvn spring-boot:run
```

The API will be listening on http://localhost:8080/api.

# API Reference

| Method | Endpoint                 | Description                                     | 
| :----- | :----------------------- | :---------------------------------------------- |
| POST   | /api/dispatch/orders     | Handles the ingestion of delivery orders.       |
| POST   | /api/dispatch/vehicles   | Handles the ingestion of vehicle details.       |
| GET    | /api/dispatch/plan       | Builds and retrieves the current dispatch plan. |

# Assignment Algorithm

- Sort orders by descending priority.
- Build k-d tree of available vehicles for fast nearest-neighbor queries.
- For each order:
  - Query nearest vehicles; compute each vehicle’s slack (remaining capacity minus package weight).
  - Filter out vehicles with insufficient capacity.
  - Track maximum/minimum slack for cost calculation.
  - Compute distance cost = minDist/maxDist and slack cost = 1 – (minSlack/maxSlack).
  - If best-fit vehicle is within 15 km, choose based on weighted sum (40% slack, 60% distance); otherwise pick the absolute nearest.
  - Update vehicle’s totalDistance and assignedCapacity.
  - Remove any vehicle with zero remaining capacity from the k-d tree.




