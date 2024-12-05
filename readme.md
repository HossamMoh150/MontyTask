# **Monty Task Application**

## **Overview**

The Monty Task Application is a Spring Boot project designed to manage employees and authenticate users with role-based access control. It includes REST APIs for user authentication, employee management, and search functionality. The application supports **admin** and **user** roles and is secured using **Spring Security** with JWT-based authentication.

---

## **Features**

- **Authentication:**
    - Register and Login endpoints with role-based access.
    - JWT-based security for secure API communication.

- **Employee Management:**
    - CRUD operations for employees.
    - Search functionality with filters for name, department, and skills.

- **Security:**
    - Role-based access control.
    - Custom exception handling for authentication and authorization errors.

- **Startup Initialization:**
    - Creates two users on application startup: `admin` and `user`.

---

## **Technologies Used**

- **Backend:**
    - Java 17
    - Spring Boot (Web, Security, Data JPA)
    - Hibernate

- **Database:**
    - H2 (in-memory) for development/testing

- **Other Libraries:**
    - JWT (JSON Web Tokens)
    - BCrypt for password encryption

---


## Default Users on Startup


| Username | Password         | Role                |
|----------|------------------|----------------------|
| admin    | `password123`         |ROLE_ADMIN      | 
| user     | `password123`     | ROLE_USER          |


## **Endpoints**

### **Authentication**

| HTTP Method | Endpoint         | Description              | Authorization |
|-------------|------------------|--------------------------|---------------|
| POST        | `/auth/register` | Register a new user      | Public        |
| POST        | `/auth/login`    | Login and get JWT token  | Public        |

### **Employee Management**

| HTTP Method | Endpoint                 | Description                 | Authorization         |
|-------------|--------------------------|-----------------------------|-----------------------|
| POST        | `/employees`             | Create a new employee       | Admin only            |
| GET         | `/employees`             | Get all employees           | Admin, User           |
| GET         | `/employees/{id}`        | Get an employee by ID       | Admin, User           |
| PUT         | `/employees/{id}`        | Update an existing employee | Admin only            |
| DELETE      | `/employees/{id}`        | Delete an employee          | Admin only            |
| GET         | `/employees/search`      | Search employees by filters | Admin, User           |

---

## **Getting Started**

### **Prerequisites**

- Java 17
- Maven 3.8+
- H2 Database (In-memory)

### **Run Locally**

Build the project:

bash
Copy code
```
mvn clean install
```


## H2 Console:
### http://localhost:8080/h2-console
### JDBC URL: jdbc:h2:mem:testdb
### User: sa
### Password: password

