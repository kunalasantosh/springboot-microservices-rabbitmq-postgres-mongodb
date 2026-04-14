# Spring Boot Microservices Showcase

This repository is a GitHub-ready microservice sample that shows:
- Spring Boot based microservices
- PostgreSQL for transactional order data
- MongoDB for document-oriented inventory data
- RabbitMQ for event-driven communication
- Clear service boundaries that are easy to explain in an interview

## What this project demonstrates

This repo uses a small commerce workflow because it is simple to explain and it maps well to common microservice patterns.

### Services

#### 1) order-service
- Owns order creation
- Stores orders in PostgreSQL
- Exposes REST APIs
- Publishes `OrderCreatedEvent` to RabbitMQ

#### 2) inventory-service
- Owns product inventory
- Stores inventory documents in MongoDB
- Exposes REST APIs
- Consumes `OrderCreatedEvent`
- Publishes either `InventoryReservedEvent` or `InventoryRejectedEvent`

#### 3) notification-service
- Consumes inventory outcome events
- Logs success or failure
- Represents an async downstream subscriber

#### 4) common-events
- Shared event contracts used across services

## Architecture

```text
Client
  |
  v
order-service (Spring Boot + PostgreSQL)
  |  REST: POST /api/orders
  |  Publishes: OrderCreatedEvent
  v
RabbitMQ
  |
  v
inventory-service (Spring Boot + MongoDB)
  |  Consumes: OrderCreatedEvent
  |  Publishes: InventoryReservedEvent / InventoryRejectedEvent
  v
RabbitMQ
  |
  v
notification-service (Spring Boot)
```

## Why this is a solid portfolio repo

It lets you talk through:
- Polyglot persistence: PostgreSQL for strong relational transactions and MongoDB for flexible inventory documents
- Event-driven communication through RabbitMQ instead of direct service-to-service coupling
- Independent data ownership per service
- Async processing and eventual consistency
- Clear extension points for saga, outbox, retries, dead-letter queues, tracing, and container deployment

## Tech stack

- Java 17
- Spring Boot 3.5.12
- Spring Web
- Spring Data JPA
- Spring Data MongoDB
- Spring AMQP / RabbitMQ
- PostgreSQL 18 container
- MongoDB 8 container
- RabbitMQ 4 management container
- Docker Compose
- Maven multi-module build

## Run the project locally

### 1) Start infrastructure

```bash
docker compose up -d
```

RabbitMQ Management UI:
- http://localhost:15672
- username: `guest`
- password: `guest`

### 2) Build everything

```bash
mvn clean package -DskipTests
```

### 3) Run each service

Open 3 terminals.

```bash
cd order-service && mvn spring-boot:run
```

```bash
cd inventory-service && SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/inventory_db mvn spring-boot:run
```

```bash
cd notification-service && mvn spring-boot:run
```

## API walkthrough

### Seed inventory

```bash
curl -X POST http://localhost:8082/api/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "SKU-1001",
    "name": "Mechanical Keyboard",
    "availableQuantity": 10,
    "price": 89.99
  }'
```

### Create an order

```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "SKU-1001",
    "quantity": 2,
    "unitPrice": 89.99,
    "customerEmail": "candidate@example.com"
  }'
```

### Check inventory after reservation

```bash
curl http://localhost:8082/api/inventory
```

### Check orders

```bash
curl http://localhost:8081/api/orders
```

## Interview talking points you can use

### Why PostgreSQL for orders?
- Orders are transactional records with strong consistency needs.
- A relational model fits well for reporting, constraints, and lifecycle updates.

### Why MongoDB for inventory?
- Inventory metadata tends to change faster.
- A document model works well when product attributes evolve and the shape is less rigid.

### Why RabbitMQ?
- It decouples producers from consumers.
- It supports async workflows and lets downstream services scale independently.
- It creates a better story for resilience than hardwired synchronous service chains.

### How would you improve this repo for production?
- Add transactional outbox for reliable event publishing
- Add dead-letter queues and retry policies
- Add distributed tracing and correlation IDs
- Add API gateway and service discovery if needed
- Add contract tests and integration tests with Testcontainers
- Add idempotent consumers and stronger error handling
- Add containerized service deployment and Kubernetes manifests

## Suggested GitHub repo name

`springboot-microservices-rabbitmq-postgres-mongodb-demo`

## Suggested README subtitle for your profile

Sample Spring Boot microservice system using PostgreSQL, MongoDB, and RabbitMQ to show REST APIs, async messaging, and service-level data ownership.
