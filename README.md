# BestInsurance API & OAuth Authorisation Server

BestInsurance provides a comprehensive system with the following features:

- Customer, Subscriptions, Policy, Coverage Management: Customers can register and subscribe to policies. Employees can manage customer, subcriptions, policy data.
- RESTful API: Exposes CRUD operations to manage customer, policy, and subscription data.
- OAuth2 Authentication: Secures API endpoints with OAuth2, allowing clients to access protected resources after obtaining JWT tokens from the authorization server.
- Docker Deployment: Uses Docker Compose to set up the application, database, and services.
- Liquibase: Manages database migrations and schema evolutions.

The project is built with **Spring Boot**, **Spring Data JPA**, **Spring Security (OAuth2)**, and **Liquibase** for database migrations. The project also implements unit tests using **JUnit 5** and **Testcontainers** to ensure the reliability of the application.

## Table of Contents

- [How to Run the Project](#how-to-run-the-project)
- [Project Structure](#project-structure)
- [Database Configuration](#database-configuration)
- [Security](#security)
- [Testing](#testing)
- [Contact and Acknowledgements](#contact-and-acknowledgement)


## How to Run the Project

### Prerequisites

Before running the project, ensure that you have the following software installed:

- Java 17(preferably Amazon Corretto, but any distribution should work)
- Maven or Gradle
- Docker and Docker Compose

### Run Using Docker Compose

The latest `best-insurance-api`, `best-insurance-oauth2-server` images from the project have already been pushed to a Docker Hub repo. 
So you can run the project using Docker Compose following the steps below:

1. Clone the repository:

```bash
git clone https://github.com/David-Abidoye/bestInsuranceApi.git
cd bestInsurance
```
2. Make the `run.sh` script executable:

```bash
chmod +x run.sh
```

3. Run the application using the `run.sh` script, which will pull the images from Docker Hub and start the services:

```bash
./run.sh
```

This will:

- Pull the latest `best-insurance-api`, `best-insurance-oauth2-server`, and `postgres` images from Docker Hub.
- Start the **PostgreSQL**, **API server**, **OAuth server**, and **Adminer** containers as defined in `docker/docker-compose.yml`.

When all containers are started successfully, you can access the **API server** at [http://localhost:8080](http://localhost:8080). For detailed information on each API endpoint, including query parameters, request bodies, and responses, refer to the Swagger UI available at http://localhost:8080/swagger-ui.html.

### Stopping the Project
You can stop the Docker containers using the `stop.sh` script. This script provides two options:

1. Stop the containers without removing the volumes (preserves database data):

  ```bash
  ./stop.sh
  ```
2. Stop the containers and remove the volumes (removes the database volume, which erases the database data):

  Open `stop.sh` and comment out the first line, then uncomment the second line:

  ```bash
  # docker-compose -p bi_docker -f docker/docker-compose.yml down
  docker-compose -p bi_docker -f docker/docker-compose.yml down -v --remove-orphans
  ```

### Docker Compose Configuration
The configuration of the services is defined in `docker/docker-compose.yml`. The key settings include:

- PostgreSQL Database (`db` service):
  - Username: `admin`
  - Password: `mysecretpassword`
  - Database name: `bestInsurance`
- API Server (`app-api` service):
  - Connects to the PostgreSQL database using the environment variables defined in `docker-compose.yml`.
  - OAuth2 configuration for authentication and authorization.
- OAuth Server (`oauth-server` service):
  - Configured to serve OAuth2 tokens for securing the API.
- Adminer (`adminer` service):
  - Provides a web-based interface for database management on http://localhost:8081.
 

## Project Structure

The project is structured into two modules:
- **best-insurance-api**: The core API module that handles insurance-related operations and acts as the resource server. 
- **best-insurance-oauth2-server**: The module that implements the OAuth2 authorisation server for issuing and validating JWT tokens used by the resource server.

### best-insurance-api

This module is organised into several key directories and classes:

- `/src/main/java/com/bestinsurance/api/`: The main application code.
  - config: Contains configuration classes for setting up the application, including security, OpenApi, Messaging and Domain-specific configurations.
  - controller: Contains the REST API controller classes, which expose the API endpoints for handling customer, policy, coverages and subscription data.
  - dto: Data Transfer Object (DTO) classes used for mapping data between domain/model classes and input/output API representations.
  - exception: Classes that handle custom exceptions and error responses.
  - helper: Utility classes that provide shared functionality throughout the application.
  - mapper: Classes responsible for mapping between DTOs and domain models.
  - model: Contains the domain (persistent entity) classes that represent the database tables, such as Customer, Policy, Subscription, etc.
  - repos: JPA repositories that handle database interactions.
  - security: Contains security-related classes.
  - service: Service classes that implement business logic. The intermediary between the controller and the persistence layer (repositories).
  - validation: Classes that define custom validation logic, including annotations and utility methods for validating domain objects and incoming requests.
- `/src/main/resources`: Contains configuration files, including Liquibase configuration(`/db.changelog`) for database schema management and `application.properties`/`application-dev.properties` for configuring the Spring Boot application, including database settings, server port, and other environment-specific properties.
- `/src/test`: Unit and integration tests, utilising JUnit 5 and Testcontainers for testing against real Docker containers.


### best-insurance-oauth2-server

This module is structured to provide OAuth2 authorisation and token management:

- `/src/main/java/com/bestinsurance/oauth2`: Contains the core OAuth2 server code and configurations.
  - config: Contains configuration classes related to OAuth2 authorisation and token management.
    - OauthServerConfig.java: The OAuth2 authorisation server configuration. This class configures the OAuth2 server to authenticate clients and issue JWT tokens.

## Database Configuration
The database is configured to run inside a Docker container, using the PostgreSQL image. The following configurations are used for the database connection:

- Database Name: `bestInsurance`
- User: `admin`
- Password: `mysecretpassword`

The Liquibase migration tool is used to handle database schema changes. The migration files are located in the `src/main/resources/db/changelog` directory.

### Sample Data
The project includes sample data for countries, states, cities, customers, policies, and subscriptions. The `SampleDataLoader` class loads the sample data into the database upon startup. You can disable this by setting `DATALOADER_LOADSAMPLE` to false in the `docker/docker-compose.yml`.

## Security
This project secures the API using OAuth 2.0 with JWT tokens:

- OAuth2 Authorisation Server (`best-insurance-oauth2-server`): Issues JWT tokens after successful login.
- Resource Server (`best-insurance-api`): The backend API that validates the JWT tokens before allowing access to protected resources.  It ensures that only authorized clients can access the data.
- Client: Any client application (e.g., Swagger UI, Postman, or frontend clients) that authenticates using OAuth2 to obtain access tokens and interacts with the resource server on behalf of the user.

#### The authentication flow works as follows:

1. Client Request: The client requests access to a protected resource from the resource server.
2. Token Validation: If no JWT token is found in the request, the client is redirected to the OAuth2 Authorisation Server for authentication.
3. Login and Token Exchange: The client logs in with valid credentials and receives an authorisation code. The client then exchanges this code for a JWT token from the authorization server.
4. Token Submission: The client includes the JWT token in the Authorisation header when making requests to the resource server.
5. Token Validation by Resource Server: The resource server validates the JWT token's integrity and user details before granting access to the requested resource.

#### Client Authentication Credentials
To authenticate and obtain access tokens, the client application must provide the following credentials to the authorization server:

- **Client ID**: `bestInsurance-client`
- **Client Secret**: `secret`

These credentials are used to authenticate the client application and ensure that only authorised clients can request tokens on behalf of users.

#### User Roles, Permissions, and Registered Users
The project defines several user roles, each with different access levels to the API. The following users are pre-registered with the OAuth2 authorization server, each assigned a specific role. These users authenticate with the password `password` and can grant permission to a client application (e.g., Swagger UI or Postman) to access the API on their behalf.

##### username(ROLE)
- admin (ADMIN): Full access to all services and resources.
- front_office_user (FRONT_OFFICE): Access to customer-facing services, such as viewing and managing customer data.
- back_office_user (BACK_OFFICE): Access to employee-facing services, such as managing policies and subscriptions.
- customer (CUSTOMER): Limited access, allowing customers to access and update their data.

Each of these users can authenticate and, upon successful login, grant the client application the ability to obtain a JWT token, which can then be used to access protected resources according to their assigned roles and permissions. Users with different roles (e.g., `ADMIN`, `FRONT_OFFICE`, etc.) can access different sets of endpoints.

## Testing
Unit tests are implemented using JUnit 5. Testcontainers is used to spin up a PostgreSQL container for integration tests.

## Contact and Acknowledgements
- **Author**: David Abidoye
- **LinkedIn**: https://www.linkedin.com/in/david-abidoye


