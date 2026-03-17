# Secure Case Management API

Production-style backend REST API for managing cases, assignments, and comments with secure authentication and role-based access control.

This project demonstrates real-world backend engineering practices including JWT authentication, Dockerized deployment, layered architecture, and secure API design.

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- MySQL
- Docker / Docker Compose
- Swagger / OpenAPI
- JPA / Hibernate

## Features

### Authentication & Security

- JWT-based authentication
- Refresh token flow
- Role-based access control
- Password hashing with BCrypt
- Secured API endpoints

### API Architecture

- Layered architecture
  - Controller
  - Service
  - Repository
- DTO separation
- Global exception handling
- Input validation

### Infrastructure

- Dockerized Spring Boot application
- Dockerized MySQL database
- Docker Compose environment
- Swagger API documentation

## Running the Project

### 1 Clone the repository

git clone https://github.com/yourusername/secure-case-management-api.git

cd secure-case-management-api

### 2 Run with Docker

docker-compose up --build

### 3 Access the API

Swagger UI:

http://localhost:8080/swagger-ui/index.html

## Database

The MySQL database runs inside a Docker container.

Connection example (MySQL Workbench):

Host: localhost
Port: 3307
Username: root
Password: root

## Project Goals

This project was built to demonstrate:

- Secure REST API development
- JWT authentication and refresh tokens
- Dockerized backend services
- Clean layered architecture
- Production-style backend practices
