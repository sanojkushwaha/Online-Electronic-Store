# 🛒 Online Electronic Store API

<p align="center">
  A secure, RESTful backend for a modern online electronics store.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25">
  <img src="https://img.shields.io/badge/Spring%20Boot-4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 4">
  <img src="https://img.shields.io/badge/MySQL-8+-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/API-OpenAPI%203.1-6BA539?style=for-the-badge&logo=swagger&logoColor=white" alt="OpenAPI 3.1">
</p>

<p align="center">
  <a href="#quick-start">Quick start</a> ·
  <a href="#api-reference">API reference</a> ·
  <a href="#documentation">Swagger UI</a> ·
<a href="#security">Security</a>
</p>

<p align="center">
  <a href="https://online-electronic-store-api.onrender.com/api/swagger-ui/index.html">
    <img src="https://img.shields.io/badge/Open%20Swagger%20UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Open Swagger UI">
  </a>
</p>

## Overview

Online Electronic Store API powers an e-commerce platform where customers can browse electronics, manage carts, place orders, and sign in securely. Administrators can manage the catalogue and categories, while OpenAPI documentation makes every endpoint easy to explore and test.

## ✨ Key features

- **Secure authentication** — JWT-based, stateless authentication with BCrypt password hashing.
- **Google sign-in** — Authenticate with a verified Google ID token.
- **Role-based access** — Separate `USER` and `ADMIN` permissions for customer and catalogue operations.
- **Product catalogue** — Products, categories, keyword search, pagination, sorting, and live-product filtering.
- **Cart and checkout** — Add products, update quantities, clear carts, and create orders.
- **Order lifecycle** — Track order and payment statuses from pending through delivery.
- **Image management** — Upload and serve product and user profile images.
- **Interactive docs** — Explore requests and responses through Swagger UI.

## 🧰 Tech stack

| Area | Technology |
| --- | --- |
| Language | Java 25 |
| Framework | Spring Boot 4, Spring MVC |
| Data | Spring Data JPA, Hibernate, MySQL |
| Security | Spring Security, JJWT, BCrypt |
| Validation | Jakarta Bean Validation |
| Documentation | Springdoc OpenAPI / Swagger UI |
| Build tool | Maven Wrapper |

## Project structure

```text
src/main/java/com/webapp/onlineelectronicstore/
├── config/          # Security, OpenAPI, and mapper configuration
├── controllers/     # REST API endpoints
├── dtos/            # Request and response models
├── entites/         # JPA entities
├── exceptions/      # API exception handling
├── repositories/    # Database access layer
├── security/        # JWT filter and user-details implementation
└── services/        # Business logic
```

## Quick start

### Prerequisites

- JDK 25
- MySQL 8 or later
- Git (optional)

### 1. Clone the repository

```bash
git clone https://github.com/sanojkushwaha/Online-Electronic-Store.git
cd Online-Electronic-Store
```

### 2. Create the database

```sql
CREATE DATABASE electronic_store;
```

### 3. Configure the application

Update `src/main/resources/application.properties` with your local settings. Never commit real credentials or production secrets.

```properties
server.port=9090
server.servlet.context-path=/api

spring.datasource.url=jdbc:mysql://localhost:3306/electronic_store
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

jwt.secret=YOUR_BASE64_ENCODED_JWT_SECRET
jwt.expiration=18000000

app.google.client_id=YOUR_GOOGLE_OAUTH_CLIENT_ID
app.google_default_password=CHANGE_THIS_DEFAULT_VALUE

user.profile.image.path=/images/users/
product.image.path=/images/products/
```

Ensure the configured image directories exist and are writable by the application.

### 4. Run the API

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

The API starts at **`http://localhost:9090/api`**.

## Documentation

Once the service is running, open Swagger UI to inspect endpoints, schemas, and responses:

```text
http://localhost:9090/api/swagger-ui/index.html
```

[**Open Swagger UI locally →**](http://localhost:9090/api/swagger-ui/index.html)

Swagger UI lets you authorize with a JWT, enter request values, and send requests without a separate API client. Start the backend first; the link is available only on the machine where the API is running.

## Authentication

Sign in to receive a JWT:

```http
POST /api/auth/generate-token
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "your-password"
}
```

Send the returned token with protected requests:

```http
Authorization: Bearer <jwt-token>
```

Google sign-in is available at `POST /api/auth/login-with-google` and accepts a Google ID token.

## API reference

All endpoints are relative to `/api`. Collection endpoints support pagination and sorting where applicable; Swagger UI is the source of truth for complete schemas.

| Area | Method | Endpoint | Description |
| --- | --- | --- | --- |
| Authentication | `POST` | `/auth/generate-token` | Sign in with username and password. |
| Authentication | `POST` | `/auth/login-with-google` | Sign in with a Google ID token. |
| Users | `GET`, `POST` | `/users` | List users or register a user. |
| Users | `GET`, `PUT`, `DELETE` | `/users/{userId}` | Retrieve, update, or delete a user. |
| Users | `GET` | `/users/email/{email}` | Find a user by email address. |
| Users | `GET` | `/users/search/{keywords}` | Search users. |
| Users | `GET`, `POST` | `/users/image/{userId}` | Retrieve or upload a profile image (`uploadImage`). |
| Categories | `GET`, `POST` | `/categories` | List or create categories. |
| Categories | `GET`, `PUT`, `DELETE` | `/categories/{categoryId}` | Retrieve, update, or delete a category. |
| Products | `GET`, `POST` | `/products` | List or create products. |
| Products | `GET`, `PUT`, `DELETE` | `/products/{productId}` | Retrieve, update, or delete a product. |
| Products | `GET` | `/products/search/{keyword}` | Search the catalogue. |
| Products | `GET` | `/products/category/{categoryId}` | List products in a category. |
| Products | `GET` | `/products/live` | List live products. |
| Products | `GET`, `POST` | `/products/image/{productId}` | Retrieve or upload a product image (`productImage`). |
| Carts | `GET`, `POST`, `DELETE` | `/carts/{userId}` | Retrieve a cart, add an item, or clear the cart. |
| Carts | `PUT`, `DELETE` | `/carts/{userId}/items/{cartItemId}` | Update item quantity or remove an item. |
| Orders | `GET` | `/orders` | List all orders. |
| Orders | `POST` | `/orders/{userId}` | Place an order from the user's cart. |
| Orders | `GET`, `DELETE` | `/orders/{orderId}` | Retrieve or cancel an order. |
| Orders | `GET` | `/orders/users/{userId}` | List a user's orders. |
| Orders | `PUT` | `/orders/{orderId}/status` | Update an order status. |
| Orders | `PUT` | `/orders/{orderId}/payment` | Update a payment status. |

### Example: add an item to a cart

```http
POST /api/carts/{userId}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "productId": "product-id",
  "quantity": 1
}
```

## Authorization

| Role | Access |
| --- | --- |
| Public | Authentication, registration, product/category reads, and image reads. |
| `ROLE_USER` | Cart and order operations. |
| `ROLE_ADMIN` | Product and category creation, updates, and deletion. |

## Build and test

Create the runnable JAR:

```powershell
.\mvnw.cmd clean package
```

Run tests:

```powershell
.\mvnw.cmd test
```

The generated application JAR is written to `target/`.

## Security

- Keep database credentials, JWT secrets, default passwords, and OAuth client IDs outside version control.
- Use environment variables or a deployment-specific secret manager in production.
- Restrict CORS to the deployed frontend origin before release.
- Use database migrations and review `spring.jpa.hibernate.ddl-auto` before production deployment.
- Store uploaded images in persistent, access-controlled storage in production.

## License

The OpenAPI documentation identifies this API as **Apache-2.0**. Add an explicit `LICENSE` file to the repository to make the licensing terms clear for all users and contributors.

---
Author:-> 
 Built by ❤️ Java Backend Engineer, [Sanoj Kushwaha](https://github.com/sanojkushwaha) 
