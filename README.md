# Backend Mastery

A backend learning project centered on a **Spring Boot JWT authentication system**, with smaller demos for **Spring AOP** and **Java Dynamic Proxies**.

## 🧰 Tech Stack

- ☕ Java
- 🌱 Spring Boot
- 🔐 Spring Security
- 🪪 JWT
- 🗄️ Spring Data JPA
- 🐬 MySQL
- 📨 Apache Kafka
- 🛠️ Gradle

## 📖 Project Overview

This repository mainly demonstrates a **role-based authentication and authorization system** built using Spring Boot and Spring Security.

It supports:

- user signup and login
- BCrypt password hashing
- JWT-based authentication
- role-based access control
- refresh token-based access token renewal
- Kafka notifications for signup and login events

The repository also includes small examples of:
- **Spring AOP** for method-level logging
- **Java Dynamic Proxies** for understanding proxy-based interception

## 🔐 Auth Flow

### 👥 Roles

Users are assigned roles such as `USER` and `ADMIN`.  
Protected APIs are secured using Spring Security and role checks such as `@PreAuthorize`.

### 🪪 Access Token + Refresh Token Mechanism

This project uses a **two-token approach**:

- **Access Token**
  - short-lived JWT
  - sent in the `Authorization: Bearer <token>` header
  - used to access protected endpoints

- **Refresh Token**
  - stored in the database
  - used to generate a new access token after expiry
  - helps avoid forcing the user to log in again frequently

### 🔄 Request Flow

- On **signup/login**, the server returns:
  - an access token
  - a refresh token

- On protected requests:
  - `JwtAuthFilter` extracts and validates the JWT
  - Spring Security loads the user and roles
  - access is granted only if the role matches endpoint permissions

- When the access token expires:
  - the client calls the refresh endpoint with the refresh token
  - if valid and not expired, a new access token is issued

## 🌐 Main Endpoints

### 🔓 Public

- `POST /auth/v1/signup`
- `POST /auth/v1/login`
- `POST /auth/v1/refreshAccessToken`

### 🔒 Protected

- `GET /user/profile` → `USER` or `ADMIN`
- `GET /admin/dashboard` → `ADMIN`
- `GET /square?num=<any integer>` → authenticated user (no role required)

## 🧩 Other Modules

### 🎯 Spring AOP Demo
A small example showing how cross-cutting concerns like logging can be added without changing business logic.

### 🪄 Java Dynamic Proxy Demo
A basic proxy example to understand how method interception works under the hood.

## ✅ Why This Project Is Useful

This project demonstrates practical backend concepts commonly discussed in interviews:

- stateless authentication with JWT
- refresh token workflow
- Spring Security filter chain
- role-based authorization
- database-backed token persistence
- event-driven notification using Kafka
- clean layering across controller, service, repository, and config