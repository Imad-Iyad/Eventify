<div align="center">

# **Eventify**

### **Event Management Platform, Securely and Seamlessly**

<br>

<p>
    <img src="https://img.shields.io/badge/last%20commit-september-blue?style=for-the-badge" alt="last commit">
    <img src="https://img.shields.io/badge/java-99.3%25-blue?style=for-the-badge" alt="language">
    <img src="https://img.shields.io/badge/languages-2-blue?style=for-the-badge" alt="languages">
</p>

<p>
  <strong>Built with the tools and technologies:</strong><br>
    <img src="https://img.shields.io/badge/XML-gray?style=for-the-badge" alt="XML">
    <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
    <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
    <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white" alt="JWT">
</p>

</div>

---

### **Table of Contents**
* [Overview](#overview)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Usage](#usage)
  * [Testing](#testing)

---

## **Overview**

Eventify is a comprehensive event management platform built with **Spring Boot**. It supports both public and private events with secure JWT-based authentication and role-based access control. The platform allows event organizers to create, manage, and track events, while attendees can register and confirm their participation via QR codes.

### **Why Eventify?**

This project simplifies the creation and management of events through:

-   **🛡️ Security & Authentication**: Implements JWT-based authentication and role-based access to secure user data and event details.
-   **📅 Event Management**: Create, update, and delete events; manage registrations and track attendance.
-   **🎟️ Invitation Management**: Send, track, and manage invitations for private events.
-   **📦 Containerized Deployment**: Dockerized for easy deployment and scalability.
-   **📄 Auto-Generated API Docs**: Uses Swagger/OpenAPI for automatic API documentation.
-   **⚙️ Robust Architecture**: Clean code architecture with Controllers, Services, Repositories, and Mappers for maintainability.

---

## **Getting Started**

### **Prerequisites**

This project requires the following dependencies:

- **Programming Language**: Java
- **Package Manager**: Maven
- **Container Runtime**: Docker

### **Installation**

To build and install Eventify, follow these steps:

1. **Clone the repository:**
    ```sh
    git clone https://github.com/Imad-Iyad/Eventify
    ```

2. **Navigate to the project directory:**
    ```sh
    cd Eventify
    ```

3. **Install the dependencies:**

    **Using `docker`:**
    ```sh
    docker build -t Imad-Iyad/eventify .
    ```

    **Using `maven`:**
    ```sh
    mvn install
    ```

### **Usage**

To run Eventify:

**Using `docker`:**
```sh
docker run -it {image_name}
