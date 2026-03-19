# Payment System - Account Service

## Overview
Account Service is a microservice responsible for managing bank accounts in the Distributed Payment Processing System.

## Tech Stack
- Java 17
- Spring Boot 4.x
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## Features
- Create bank account
- Get account details
- Deposit money
- Withdraw money
- Input validation
- Global exception handling

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/accounts | Create new account |
| GET | /api/accounts/{id} | Get account by ID |
| PUT | /api/accounts/{id}/deposit | Deposit money |
| PUT | /api/accounts/{id}/withdraw | Withdraw money |

## How to Run

### Prerequisites
- Java 17+
- PostgreSQL
- Maven

### Steps
1. Create database
```sql
CREATE DATABASE accountdb;
```
2. Update `application.properties` with your PostgreSQL credentials
3. Run the application
```bash
mvn spring-boot:run
```
4. Service runs on port 8081

## Project Structure
```
account-service/
├── entity/          → Database entities
├── repository/      → Data access layer
├── service/         → Business logic
├── controller/      → REST API endpoints
├── dto/             → Data Transfer Objects
├── mapper/          → Entity to DTO conversion
└── exception/       → Exception handling
```