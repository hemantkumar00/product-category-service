# 🛍️ Product Category Service

A Spring Boot microservice for managing products and categories with support for advanced search, filtering, pagination, and Redis caching.

---

## 📌 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
    - [Prerequisites](#-prerequisites)
    - [Build & Run](#-build--run)
    - [Configuration](#-configuration)
    - [Running Tests](#-running-tests)
- [API Endpoints](#-api-endpoints)
- [Project Structure](#-project-structure)
- [Author](#-author)
- [Show Your Support](#-show-your-support)

---

## ✅ Features

- CRUD operations for products and categories
- Advanced search with multi-field filtering and sorting
- Pagination support for large data sets
- Redis caching for performance-optimized search results
- Global exception handling and input validation

---

## 🧰 Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Data Redis**
- **Hibernate Validator**
- **JUnit 5 + Mockito**
- **Maven**

---

## 🚀 Getting Started

### ✅ Prerequisites

Make sure you have the following installed:

- Java 17+
- Maven 3.8+
- Redis server (local or remote)

### ▶️ Build & Run


#### Clone the repository

```bash
git clone https://github.com/your-username/product-category-service.git
cd product-category-service
```

#### Build the project
```bash
mvn clean install
```

#### Run the application
```bash
mvn spring-boot:run
```
### ⚙️ Configuration

Update Redis and database configuration in:

```bash
src/main/resources/application.properties
```

properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/productdb
spring.datasource.username=root
spring.datasource.password=root

spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### 🧪 Running Tests

```bash
mvn test
```

## 📡 API Endpoints

### 📦 Products

| Method | Endpoint         | Description                |
|--------|------------------|----------------------------|
| POST   | /products/       | Create a product           |  
| GET    | /products/{id}   | Get product by ID          |
| PATCH  | /products/{id}   | Update a product           |
| DELETE | /products/{id}   | Delete a product           |

### 🗂️ Categories
| Method | Endpoint         | Description                |
|--------|------------------|----------------------------|
| POST   | /categories/     | Create a category          |
| GET    | /categories/{id} | Get category by ID         |
| PATCH  | /categories/{id} | Update a category          |
| DELETE | /categories/{id} | Delete a category          |
| GET    | /categories/     | Get all categories         |

### 🔍 Search

| Method | Endpoint           | Description                                             |
|--------|--------------------|---------------------------------------------------------|
| GET    | /search/           | Search products with filtering, sorting, and pagination |
| GET    | /search/byCategory | Search products with simple keyword matching            |



## 🗃️ Project Structure

```plaintext
src/
├── configs/           # Configuration classes
├── controllerAdvice/  # Global exception handlers
├── controllers/       # REST controllers
├── dtos/              # Data Transfer Objects
├── exceptions/        # Custom exceptions
├── models/            # Entity models
├── repositories/      # JPA repositories
└── service/           # Service layer 
```


## 🙋‍♂️ Author

Hemant Kumar
- **GitHub**: [hemantkumar00](https://github.com/hemantkumar00)
- **LinkedIn**: [Hemant Kumar](https://www.linkedin.com/in/hemant00/)

## 🌟 Show Your Support
If you find this project useful, please consider showing your support:

⭐ Star this repo

📢 Share it with others

💡 Contribute via pull requests