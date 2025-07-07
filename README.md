# ProjectTest: Project and Vacancy Management System (Backend)

This repository contains the backend part of the project and job management system developed as part of the test task.
The system provides a REST API for performing CRUD (Create, Read, Update, Delete) operations on projects and related
vacancies.


## General description

***The project is a RESTful API that allows you to:***

- Manage projects: Create, view (list or by ID), update and delete information about projects. Each
  project has a name, area, required experience, description and deadline.

- Manage vacancies: Add vacancies to specific projects, view them, update and delete them. Vacancies include
  a name, area, experience, country and description.

***Technologies Used***

- Programming Language: Java 22

- Framework: Spring Boot 3.3.8

- Project Builder: Maven

- Database: PostgreSQL 16

- ORM: Spring Data JPA / Hibernate

- Database Migrations: Liquibase 4.29.0

- Validation: Jakarta Bean Validation (via Spring Boot Starter Validation)

- Object Mapping: MapStruct 1.5.5.Final

- Boilerplate Code Reduction: Lombok 1.18.34

- API Documentation: Springdoc OpenAPI UI (Swagger UI) 2.5.0

- Containerization: Docker, Docker Compose

- Deployment: Railway

### Project Structure

***The project has a standard Spring Boot structure:***

- org.project.controller: REST controllers for handling HTTP requests.

- org.project.service: Service layer containing business logic.

- org.project.repository: Spring Data JPA interfaces for interacting with the database.

- org.project.entity: JPA entities representing database tables.

- org.project.dto: Data Transfer Objects (DTOs) for API requests and responses.

- org.project.dto.mapper: MapStruct interfaces for converting entities to DTOs and back.

- org.project.exception: Custom exceptions.

- org.project.handler: Global exception handler.

## Instructions for running locally
To run the project locally, you will need Docker and Docker Compose installed.

Clone the repository:

git clone https://github.com/Kostiantyn19899/ProjectTest.git
cd ProjectTest # Go to the project root directory

To run locally with Docker Compose, you will need the src/main/resources/application-local.properties file

Important: Make sure that the port in spring.datasource.url (5432 or 5433) matches the one you mapped to port 5432 of the PostgreSQL container on your host machine in docker-compose.yml (e.g. ports: - "5432:5432" or ports: - "5433:
5432").

Build your Spring Boot application JAR:
Make sure you are in the project root directory (where the pom.xml is located).

mvn clean package -DskipTests

This will create an executable JAR file in the target/ directory.

Start the PostgreSQL database using Docker Compose:
Make sure the Dockerfile and docker-compose.yml files are in the root directory of your project.

docker-compose up -d postgres

docker-compose ps will help you check the status of the postgres container.

Run the Spring Boot application locally (via IDE or JAR):

Via IntelliJ IDEA: In the run configuration of your Spring Boot application (App or ProjectTestApplication), in the "
Active profiles" field, enter local.

Via the command line (if you are running a JAR):

java -jar target/ProjectTest-1.0-SNAPSHOT.jar --spring.profiles.active=local
Документация API (Swagger UI)
После успешного запуска приложения, документация API будет доступна через Swagger UI по следующему адресу:
http://localhost:8080/swagger-ui/index.html
https://projecttest-production-d14c.up.railway.app/swagger-ui/index.html
Here you can view all available endpoints, their parameters, request/response models and execute requests directly
from the browser.

API endpoints and request examples
You can use Postman, Insomnia, curl or Swagger UI to test the API.

Project Management

1. Get a list of projects

GET /projects

Response example (200 OK):

[
{
"id": 1,
"name": "Project Alpha",
"field": "IT",
"experience": "Mid",
"description": "IT project description.",
"deadline": "2025-12-31"
}
]

2. Create a new project

POST /projects

Header: Content-Type: application/json

Request body example:

{
"name": "New Awesome Project",
"field": "Software Development",
"experience": "Senior",
"description": "Developing a new microservice architecture.",
"deadline": "2026-06-30"
}

Response example (201 Created):

{
"id": 1,
"name": "New Awesome Project",
"field": "Software Development",
"experience": "Senior",
"description": "Developing a new microservice architecture.",
"deadline": "2026-06-30"
}

3. Getting a project by ID

GET /projects/{id}

Response example (200 OK):

{
"id": 1,
"name": "New Awesome Project",
"field": "Software Development",
"experience": "Senior",
"description": "Developing a new microservice architecture.",
"deadline": "2026-06-30"
}

Response example (404 Not Found):

Project not found with id: 99

4. Editing a project

PUT /projects/{id}

Header: Content-Type: application/json

Request body example:

{
"name": "Updated Project Name",
"field": "Updated Field",
"experience": "Updated Experience",
"description": "Updated Description for the project.",
"deadline": "2026-07-31"
}

Response example (200 OK):

{
"id": 1,
"name": "Updated Project Name",
"field": "Updated Field",
"experience": "Updated Experience",
"description": "Updated Description for the project.",
"deadline": "2026-07-31"
}

5. Deleting a project

DELETE /projects/{id}

Response example (200 OK): Empty response body.

Managing vacancies

1. Getting a list of vacancies for a specific project

GET /projects/{id}/vacancies

Response example (200 OK):

[
{
"id": 101,
"name": "Java Developer",
"field": "Backend",
"experience": "3 years",
"country": "Germany",
"description": "Looking for a skilled Java developer.",
"projectId": 1
}
]

2. Adding a vacancy to a project

POST /projects/{id}/vacancies

Header: Content-Type: application/json

Request body example:

{
"name": "Frontend Developer",
"field": "Web Development",
"experience": "2 years",
"country": "France",
"description": "Seeking a React specialist for our new project."
}

Response example (201 Created):

{
"id": 102,
"name": "Frontend Developer",
"field": "Web Development",
"experience": "2 years",
"country": "France",
"description": "Seeking a React specialist for our new project.",
"projectId": 1
}

3. Editing a vacancy

PUT /vacancies/{id}

Header: Content-Type: application/json

Request body example:

{
"name": "Senior Frontend Developer",
"field": "Web Development",
"experience": "5 years",
"country": "France",
"description": "Updated description for senior React specialist."
}

Response example (200 OK):

{
"id": 102,
"name": "Senior Frontend Developer",
"field": "Web Development",
"experience": "5 years",
"country": "France",
"description": "Updated description for senior React specialist.",
"projectId": 1
}

4. Deleting a vacancy

DELETE /vacancies/{id}

Response example (204 No Content): Empty response body.

Deploy to Railway
The project is configured for deployment to Railway.

Link to the deployed application:
https://projecttest-production-d14c.up.railway.app

Instructions for testing the deployed project:
You can use the above sample requests with Postman, Insomnia or curl.
Swagger UI documentation will be available at
https://projecttest-production-d14c.up.railway.app/swagger-ui/index.html

Thank you for your attention to my project!
Konstantin Serikov (https://www.linkedin.com/in/kostiantin-sierikov-95700b337/)