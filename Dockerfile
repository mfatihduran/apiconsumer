#FROM amazoncorretto:11
#MAINTAINER gesoft.co.uk
#COPY target/eager-api-0.0.1.jar eager-api-0.0.1.jar
#ENTRYPOINT ["java","-jar","/eager-api-0.0.1.jar"]

# Use an official OpenJDK runtime as a base image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the application JAR file into the container
COPY target/eager-api-0.0.1.jar .

# Specify the command to run your application
ENTRYPOINT ["java", "-Dspring.profiles.active=test","-jar", "eager-api-0.0.1.jar"]