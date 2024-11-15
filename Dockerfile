# Use an official OpenJDK image as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Install dependencies (e.g., wget, curl)
RUN apt-get update && apt-get install -y wget

# Copy the local dockerize binary to the container
COPY ./dockerize /usr/local/bin/

# Make sure dockerize is executable
RUN chmod +x /usr/local/bin/dockerize

# Copy the Maven wrapper and pom.xml into the container
COPY mvnw . 
COPY .mvn .mvn
COPY pom.xml .

# Install dependencies (download dependencies offline)
RUN ./mvnw dependency:go-offline

# Copy the entire project into the container
COPY . .

# Package the Spring Boot application using Maven (skip tests)
RUN ./mvnw clean package -DskipTests

# Expose the port that the app runs on (Spring Boot default is 8080)
EXPOSE 8080

# Run the application using dockerize to wait for MySQL
ENTRYPOINT ["dockerize", "-wait", "tcp://db:3306", "-timeout", "60s", "java", "-jar", "target/CnotConnect-1-0.0.1-SNAPSHOT.jar"]
