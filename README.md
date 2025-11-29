# BookTicket :: Services :: User Service

## Overview

The **User Service** is a core microservice responsible for managing all aspects of user identity, authentication, and authorization within the BookTicket platform. It serves as the single source of truth for user data.

## Core Responsibilities

-   **User Registration:** Handles the creation of new user accounts.
-   **Authentication:** Manages user login and, upon successful authentication, generates and signs JSON Web Tokens (JWTs).
-   **Password Management:** Securely stores user passwords using strong hashing algorithms (BCrypt).
-   **User Profile Management:** Provides endpoints for users to view and manage their own profile information.

## Architecture
<img width="1473" height="1642" alt="User Service-2025-11-26-191334" src="https://github.com/user-attachments/assets/b3322aed-a237-40f5-af9a-15f2cbfe6c57" />


### How It Works

1.  **Data Storage:** User information is stored in a **PostgreSQL** database to ensure data integrity and consistency. Passwords are never stored in plain text; they are hashed using the **BCrypt** algorithm provided by Spring Security.
2.  **Authentication Flow:**
    -   A user sends their credentials to the `/api/v1/auth/login` endpoint, which is routed through the API Gateway to the User Service.
    -   The User Service validates the credentials against the hashed password in the database.
    -   If valid, it generates a signed **JWT** containing the user's ID, username, and roles. This token is returned to the client.
3.  **Stateless Authorization:** For all subsequent requests, the client includes the JWT in the `Authorization` header. The API Gateway validates this token without needing to contact the User Service, enabling a highly scalable and stateless security model.
4.  **Service Discovery:** The User Service registers itself with the **Eureka Service Registry**, allowing other services like the API Gateway to discover its location dynamically.

## Key Dependencies

-   **Spring Boot Starter Web:** For building the REST APIs.
-   **Spring Boot Starter Data JPA:** For database interaction using PostgreSQL.
-   **Spring Boot Starter Security:** For authentication, authorization, and password hashing.
-   **Eureka Discovery Client:** To register with the service registry.
-   **Java JWT (jjwt):** For creating and signing JSON Web Tokens.
-   **SpringDoc OpenAPI:** For generating API documentation.

## API Endpoints

The service's endpoints are exposed through the API Gateway.

### Public Authentication Endpoints
These endpoints are used for user registration and login and are publicly accessible.

-   `POST /api/v1/auth/register`: Creates a new user account.
-   `POST /api/v1/auth/login`: Authenticates a user with their email and password and returns a JWT upon success.

### Secured User Endpoints
These endpoints require a valid JWT in the `Authorization` header for access.

-   `GET /api/v1/users/me`: Retrieves the profile information for the currently authenticated user.
-   `GET /api/v1/users/{id}`: Retrieves the public profile information for any user by their ID.
-   `GET /api/v1/users`: Fetches a list of all users. (Requires `ADMIN` role).
-   `DELETE /api/v1/users/{id}`: Deletes a user account. (Requires `ADMIN` role).
