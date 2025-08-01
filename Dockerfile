# Stage 1: Build the Spring Boot application
# Use the Maven image to build the application. We call this stage “build”.
FROM maven:3.9.9 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to load dependencies. This allows Docker to cache dependencies.
COPY pom.xml .

# Load all Maven dependencies offline.
# This speeds up subsequent builds if pom.xml has not been changed.
RUN mvn dependency:go-offline

# Copy the application source code
COPY src/ ./src

# Build the application into an executable JAR file.
# -DskipTests skips execution of tests while building the image.
RUN mvn clean package -DskipTests

# Stage 2: Create the final Docker image (runtime image)
# CHANGE: Use openjdk:22-jdk-slim, which is Debian-based and has apt-get.
FROM openjdk:22-jdk-slim

# Set the working directory inside the container to run the application
WORKDIR /app

# Set netcat-traditional to use in the wait command in docker-compose.yml.
# This allows the application to wait for the database to become available.
# Now apt-get should work.
RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*

# Copy the built JAR file from the “build” stage to the final image.
COPY --from=build /app/target/ProjectTest-1.0-SNAPSHOT.jar /app/ProjectTest-1.0-SNAPSHOT.jar

# Open the port on which the Spring Boot application will run
EXPOSE 8080

# ENTRYPOINT is not specified here, as it will be overridden in docker-compose.yml
# to add the database wait logic.
