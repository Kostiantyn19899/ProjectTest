# Stage 1: Build the Spring Boot application
# Используем образ Maven для сборки приложения. Называем эту стадию "build".
FROM maven:3.9.9 AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл pom.xml для загрузки зависимостей. Это позволяет Docker кэшировать зависимости.
COPY pom.xml .

# Загружаем все зависимости Maven в оффлайн-режиме.
# Это ускоряет последующие сборки, если pom.xml не менялся.
RUN mvn dependency:go-offline

# Копируем исходный код приложения
COPY src/ ./src

# Собираем приложение в исполняемый JAR-файл.
# -DskipTests пропускает выполнение тестов во время сборки образа.
RUN mvn clean package -DskipTests

# Stage 2: Create the final Docker image (runtime image)
# ИЗМЕНЕНО: Используем openjdk:22-jdk-slim, который основан на Debian и имеет apt-get.
FROM openjdk:22-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера для запуска приложения
WORKDIR /app

# Устанавливаем netcat-traditional для использования в команде ожидания (command) в docker-compose.yml.
# Это позволяет приложению ждать, пока база данных не станет доступной.
# Теперь apt-get должен работать.
RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*

# Копируем собранный JAR-файл из стадии "build" в финальный образ.
# Убедитесь, что имя JAR-файла соответствует тому, что генерируется Maven.
COPY --from=build /app/target/ProjectTest-1.0-SNAPSHOT.jar /app/ProjectTest-1.0-SNAPSHOT.jar

# Открываем порт, на котором будет работать Spring Boot приложение
EXPOSE 8080

# ENTRYPOINT здесь не указываем, так как он будет переопределен в docker-compose.yml
# для добавления логики ожидания базы данных.
