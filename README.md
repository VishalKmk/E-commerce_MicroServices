# E-commerce Microservices Architecture

A distributed microservices-based e-commerce system built with Spring Boot, demonstrating modern backend architecture patterns including service discovery, API gateway routing, inter-service communication, and resilience patterns.

## 📋 Project Overview

This project implements a scalable e-commerce platform with multiple independent services communicating through service discovery and an API gateway. The architecture demonstrates key concepts in system design and distributed systems.

**Tech Stack:**
- **Framework:** Spring Boot 3.4+ / 3.5+
- **Service Discovery:** Spring Cloud Eureka
- **API Gateway:** Spring Cloud Gateway (WebMvc)
- **Inter-service Communication:** OpenFeign with Resilience4j
- **Database:** MySQL
- **Build Tool:** Maven
- **Java Version:** 17

## 🏗️ Architecture

### Microservices

```
┌─────────────────────────────────────────────────────────┐
│                   API Gateway (8080)                     │
│              Load balancing & routing layer              │
└────────────────┬─────────────────────────────────────────┘
                 │
         ┌───────┼────────┐
         │       │        │
    ┌────▼──┐ ┌──▼─────┐ │
    │Eureka │ │ Product│ │
    │Server │ │Service │ │
    │(8761) │ │ (8081) │ │
    └───────┘ └────────┘ │
                         │
                    ┌────▼─────┐
                    │  Orders  │
                    │ Service  │
                    │  (8082)  │
                    └──────────┘
```

### Services

1. **Eureka Server (Port 8761)**
   - Service discovery and registration center
   - Maintains a registry of all active microservices
   - Enables dynamic service discovery

2. **Product Service (Port 8081)**
   - Manages product catalog and inventory
   - Exposes REST endpoints for product operations
   - Database: `ecommerce.products` table

3. **Orders Service (Port 8082)**
   - Handles order placement and management
   - Communicates with Product Service via Feign client
   - Implements circuit breaker pattern for resilience
   - Database: `ecommerce.orders` table

4. **API Gateway (Port 8080)**
   - Single entry point for all client requests
   - Routes requests to appropriate microservices
   - Load balancing support

## 🚀 Getting Started

### Prerequisites

- Java 17+
- MySQL 8.0+
- Maven 3.9+

### Database Setup

Create the MySQL database and tables:

```sql
CREATE DATABASE ecommerce;

USE ecommerce;

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL
);

CREATE TABLE orders (
    orderId BIGINT AUTO_INCREMENT PRIMARY KEY,
    productId BIGINT NOT NULL,
    quantity INT NOT NULL,
    time DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_price DOUBLE
);
```

### Environment Configuration

Update database credentials in each service's `application.properties`:

**demo/src/main/resources/application.properties:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**orders/orders/src/main/resources/application.properties:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Services

#### Option 1: Using Batch Script (Windows)

Run the provided batch script to start all services in sequence:

```bash
start-services.bat
```

This script:
- Starts Eureka Server first (waits 20 seconds)
- Starts Product Service (waits 20 seconds)
- Starts Orders Service (waits 20 seconds)
- Starts API Gateway
- Provides interactive menu to monitor and stop services

#### Option 2: Manual Startup

Start services in separate terminal windows:

```bash
# Terminal 1: Eureka Server
cd server/server
mvnw spring-boot:run

# Terminal 2: Product Service
cd demo
mvnw spring-boot:run

# Terminal 3: Orders Service
cd orders/orders
mvnw spring-boot:run

# Terminal 4: API Gateway
cd gateway
mvnw spring-boot:run
```

## 📡 API Endpoints

### Through API Gateway (Recommended)

**Product Endpoints:**
```
GET    http://localhost:8080/products           # Get all products
GET    http://localhost:8080/products/{id}      # Get product by ID
POST   http://localhost:8080/products           # Create product
PUT    http://localhost:8080/products/{id}      # Update product
DELETE http://localhost:8080/products/{id}      # Delete product
```

**Order Endpoints:**
```
POST   http://localhost:8080/order              # Place new order
GET    http://localhost:8080/order              # Get all orders
GET    http://localhost:8080/order/{id}         # Get order by ID
```

### Direct Service Access

**Product Service (8081):**
```
GET/POST/PUT/DELETE http://localhost:8081/products
```

**Orders Service (8082):**
```
GET/POST http://localhost:8082/order
```

### Service Discovery Dashboard

Access the Eureka dashboard to view registered services:
```
http://localhost:8761
```

## 💡 Key Features

### Service Discovery
- Dynamic registration and discovery using Eureka
- Services automatically register on startup and deregister on shutdown
- Health checks ensure only healthy instances receive traffic

### Resilience Patterns
- **Circuit Breaker:** Prevents cascading failures when Product Service is down
- **Fallback Mechanism:** Returns default response when service is unavailable
- **Retry Logic:** Automatically retries failed requests (configurable)

### Inter-service Communication
- OpenFeign client for declarative HTTP requests
- Load balancing across service instances
- Graceful error handling with fallback strategies

### API Gateway
- Centralized routing layer
- Request load balancing
- Service abstraction from clients

## 📊 Example Usage

### Create a Product

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Laptop",
    "price": 999.99
  }'
```

### Place an Order

```bash
curl -X POST http://localhost:8080/order \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

### Get All Orders

```bash
curl http://localhost:8080/order
```

## ⚙️ Configuration Details

### Circuit Breaker Settings

Resilience4j circuit breaker configuration in `orders/orders/src/main/resources/application.properties`:

- **Sliding Window Size:** 10 calls
- **Failure Rate Threshold:** 50%
- **Minimum Calls:** 5
- **Wait Duration (Open State):** 10 seconds
- **Permitted Calls (Half-Open):** 3

### Retry Configuration

- **Max Attempts:** 3
- **Wait Duration:** 1 second

## 📁 Project Structure

```
.
├── server/                    # Eureka Server
│   └── server/
│       ├── pom.xml
│       └── src/
├── demo/                      # Product Service
│   ├── pom.xml
│   └── src/
│       ├── controller/        # REST endpoints
│       ├── service/           # Business logic
│       ├── model/             # JPA entities
│       └── repository/        # Data access
├── orders/                    # Orders Service
│   └── orders/
│       ├── pom.xml
│       └── src/
│           ├── client/        # Feign clients
│           ├── controller/
│           ├── service/
│           ├── model/
│           └── repository/
├── gateway/                   # API Gateway
│   ├── pom.xml
│   └── src/
├── .gitignore
└── start-services.bat         # Service startup script
```

## 🔍 Monitoring & Troubleshooting

### View Active Services

**Windows:**
```bash
tasklist /fi "imagename eq java.exe"
```

**Linux/Mac:**
```bash
ps aux | grep java
```

### Check Eureka Dashboard

Visit `http://localhost:8761` to see:
- Registered services
- Instance health status
- Availability zones

### View Logs

Check individual service terminals for real-time logs, or enable DEBUG logging in `application.properties`:

```properties
logging.level.org.springframework.cloud.gateway=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Circuit Breaker Status

Access circuit breaker metrics:
```
http://localhost:8082/actuator/circuitbreakers
http://localhost:8082/actuator/circuitbreakerevents
```

## 🛠️ Development

### Adding a New Microservice

1. Create a new Spring Boot application
2. Add Eureka client dependency to `pom.xml`
3. Annotate main class with `@EnableDiscoveryClient`
4. Configure in `application.properties`:
   ```properties
   eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
   ```
5. Add route in gateway's `application.yml`

### Common Issues

**Service not registering with Eureka:**
- Ensure Eureka Server is running first
- Check `eureka.client.register-with-eureka=true`
- Verify network connectivity

**Circuit breaker not activating:**
- Check failure rate threshold is exceeded
- Ensure minimum calls threshold is met
- Review circuit breaker configuration

**Orders can't fetch product details:**
- Verify Product Service is running and registered
- Check Feign client URL matches service name
- Review fallback mechanism is working

## 📈 Future Enhancements

- Add distributed tracing (Spring Cloud Sleuth + Zipkin)
- Implement message queue (RabbitMQ/Kafka) for async communication
- Add API authentication & authorization (OAuth2, JWT)
- Database migration scripts (Flyway/Liquibase)
- Containerization (Docker & Docker Compose)
- Kubernetes deployment manifests
- Comprehensive unit & integration tests
- API documentation (Swagger/SpringDoc)

## 📝 License

This project is provided as-is for educational and development purposes.

## 👤 Author

**Bishal Karmakar** - CS Engineering Student  
Passionate about backend development, system design, and building impactful microservices architectures.
