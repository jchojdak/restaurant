FROM openjdk:19-oracle
VOLUME /tmp
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]