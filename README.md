<div align="center">

# **EVENTIFY**

### **Your Go-To Platform for Seamless and Secure Event Management**

<br>

<p>
    <img src="https://img.shields.io/github/last-commit/your-username/Eventify?style=for-the-badge&logo=github&color=blue" alt="last commit">
    <img src="https://img.shields.io/github/languages/top/your-username/Eventify?style=for-the-badge&color=blue" alt="language">
    <img src="https://img.shields.io/github/languages/count/your-username/Eventify?style=for-the-badge&color=blue" alt="languages">
</p>

<p>
  <strong>Built with the tools and technologies:</strong><br>
    <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
    <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring">
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
    <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
    <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven">
</p>

</div>

---

### **Table of Contents**
* [Overview](#overview)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Usage](#usage)
* [API Documentation](#api-documentation)

---

## **Overview**

Eventify is a comprehensive backend framework tailored for building scalable, secure event management platforms. It leverages Spring Boot to deliver core functionalities such as web services, security, data persistence, and JWT authentication, all within a modular architecture.

### **Why Eventify?**

This project simplifies the development of reliable, microservice-based event management applications. The core features include:

-   **🔒 Security & Authentication**: Implements JWT-based security with role-based access control to safeguard your APIs.
-   **🚀 RESTful API Endpoints**: Provides structured endpoints for managing events, users, bookings, and venues.
-   **📦 Containerized Deployment**: Uses Docker for consistent, portable deployment across environments.
-   **📄 Auto-Generated API Docs**: Integrates Swagger/OpenAPI for seamless API documentation and testing.
-   **⚙️ Robust Error Handling**: Centralized exception management ensures clear, consistent responses.
-   **🧩 Modular Data Flow**: Utilizes DTOs and mappers for clean, maintainable data exchange.

---

## **Getting Started**

### **Prerequisites**

This project requires the following dependencies:

* **Programming Language**: Java 17+
* **Package Manager**: Maven
* **Container Runtime**: Docker
* **Database**: PostgreSQL

### **Installation**

Build Eventify from the source and install dependencies:

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/your-username/Eventify.git](https://github.com/your-username/Eventify.git)
    ```

2.  **Navigate to the project directory:**
    ```sh
    cd Eventify
    ```

3.  **Install the dependencies:**

    **Using `docker`:**
    ```sh
    docker build -t your-username/Eventify .
    ```

    **Using `maven`:**
    ```sh
    mvn install
    ```

### **Usage**

Run the project with:

**Using `docker`:**
```sh
docker run -it {image_name}
```

**Using `maven`:**
```sh
mvn spring-boot:run
```

---

## **API Documentation**

Once the project is running, you can access the interactive Swagger UI for API documentation and testing at the following URL:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
