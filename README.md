Project: Project and Vacancy Management System (Backend)
This repository contains the backend part of the project and vacancy management system, developed as part of a test
assignment. The system provides a REST API for performing CRUD (Create, Read, Update, Delete) operations on projects and
their associated vacancies.

Project Description
The project is a RESTful API that allows you to:

Manage Projects: Create, view (list or by ID), update, and delete project information. Each project has a name, field,
required experience, description, and deadline.

Manage Vacancies: Add vacancies to specific projects, view them, update, and delete. Vacancies include a name, field,
experience, country, and description.

Technologies Used
Programming Language: Java 22

Framework: Spring Boot 3.3.8

Build Tool: Maven

Database: PostgreSQL 16

ORM: Spring Data JPA / Hibernate

Database Migrations: Liquibase 4.29.0

Validation: Jakarta Bean Validation (via Spring Boot Starter Validation)

Object Mapping: MapStruct 1.5.5.Final

Boilerplate Reduction: Lombok 1.18.34

API Documentation: Springdoc OpenAPI UI (Swagger UI) 2.5.0

Testing: JUnit 5, Mockito, Spring Boot Starter Test

Containerization: Docker, Docker Compose

Project Structure
The project has a standard Spring Boot structure:

org.project.controller: REST controllers for handling HTTP requests.

org.project.service: Service layer containing business logic.

org.project.repository: Spring Data JPA interfaces for database interaction.

org.project.entity: JPA entities representing database tables.

org.project.dto: Data Transfer Objects (DTOs) for API requests and responses.

org.project.dto.mapper: MapStruct interfaces for converting entities to DTOs and vice versa.

org.project.exception: Custom exceptions.

org.project.handler: Global exception handler.

Local Setup Instructions
To run the project locally, you will need Docker and Docker Compose installed.

Clone the repository:

git clone https://github.com/Kostiantyn19899/ProjectTest.git
cd ProjectTest # Navigate to the project's root directory

Ensure your pom.xml is correctly configured:

The com.h2database:h2 dependency should have scope=test.

The spring-boot-maven-plugin version tag should be removed to inherit from the parent POM.

The maven-compiler-plugin version should be 3.11.0 or higher.

The src/main/resources/application.properties file should either be empty regarding DataSource settings or contain only
general settings that do not conflict with Docker Compose environment variables.

Build your Spring Boot application's JAR file:
Make sure you are in the project's root directory (where pom.xml is located).

mvn clean package -DskipTests

This will create an executable JAR file in the target/ directory.

Run the application using Docker Compose:
Ensure that Dockerfile and docker-compose.yml are in the root directory of your project.

docker-compose up --build -d

--build: Rebuilds your application's image (based on Dockerfile).

-d: Runs the containers in detached mode (in the background).

Note: If you encounter a "port is already allocated" error for port 5432 (PostgreSQL) or 8080 (application), you will
need to either free up that port on your host machine or change it in the docker-compose.yml file (e.g., to 5433:5432
for PostgreSQL or 8081:8080 for the application).

Check container status:

docker-compose ps

Both containers (postgres and project) should be in the Up state.

API Documentation (Swagger UI)
After successfully starting the application, the API documentation will be available via Swagger UI at the following
address:
http://localhost:8080/swagger-ui/index.html
(If you changed the application's port to 8081, then http://localhost:8081/swagger-ui/index.html)

Here you can view all available endpoints, their parameters, request/response models, and execute requests directly from
your browser.

API Endpoints and Example Requests
You can use Postman, Insomnia, curl, or Swagger UI to test the API.

Project Management

1. Get a list of projects

GET /projects

Example response (200 OK):

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

Example request body:

{
"name": "New Awesome Project",
"field": "Software Development",
"experience": "Senior",
"description": "Developing a new microservice architecture.",
"deadline": "2026-06-30"
}

Example response (201 Created):

{
"id": 1,
"name": "New Awesome Project",
"field": "Software Development",
"experience": "Senior",
"description": "Developing a new microservice architecture.",
"deadline": "2026-06-30"
}

3. Get project by ID

GET /projects/{id}

Example response (200 OK):

{
"id": 1,
"name": "New Awesome Project",
"field": "Software Development",
"experience": "Senior",
"description": "Developing a new microservice architecture.",
"deadline": "2026-06-30"
}

Example response (404 Not Found):

Project not found with id: 99

4. Edit a project

PUT /projects/{id}

Header: Content-Type: application/json

Example request body:

{
"name": "Updated Project Name",
"field": "Updated Field",
"experience": "Updated Experience",
"description": "Updated Description for the project.",
"deadline": "2026-07-31"
}

Example response (200 OK):

{
"id": 1,
"name": "Updated Project Name",
"field": "Updated Field",
"experience": "Updated Experience",
"description": "Updated Description for the project.",
"deadline": "2026-07-31"
}

5. Delete a project

DELETE /projects/{id}

Example response (200 OK): Empty response body.

Vacancy Management

1. Get a list of vacancies for a specific project

GET /projects/{id}/vacancies

Example response (200 OK):

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

2. Add a vacancy to a project

POST /projects/{id}/vacancies

Header: Content-Type: application/json

Example request body:

{
"name": "Frontend Developer",
"field": "Web Development",
"experience": "2 years",
"country": "France",
"description": "Seeking a React specialist for our new project."
}

Example response (201 Created):

{
"id": 102,
"name": "Frontend Developer",
"field": "Web Development",
"experience": "2 years",
"country": "France",
"description": "Seeking a React specialist for our new project.",
"projectId": 1
}

3. Edit a vacancy

PUT /vacancies/{id}

Header: Content-Type: application/json

Example request body:

{
"name": "Senior Frontend Developer",
"field": "Web Development",
"experience": "5 years",
"country": "France",
"description": "Updated description for senior React specialist."
}

Example response (200 OK):

{
"id": 102,
"name": "Senior Frontend Developer",
"field": "Web Development",
"experience": "5 years",
"country": "France",
"description": "Updated description for senior React specialist.",
"projectId": 1
}

4. Delete a vacancy

DELETE /vacancies/{id}

Example response (204 No Content): Empty response body.

Deployment
Link to the deployed application:
https://projecttest-nagb.onrender.com

Instructions for testing the deployed project:
You can use the example requests above with Postman, Insomnia, or curl, replacing localhost:8080 with the public URL of
your deployed application.

Thank you for your attention to my project!