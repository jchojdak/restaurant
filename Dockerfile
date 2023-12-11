FROM gradle:8.5.0-jdk17-alpine AS build
COPY . .
RUN gradle build --no-daemon

FROM openjdk:17
COPY build/libs/restaurant-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]