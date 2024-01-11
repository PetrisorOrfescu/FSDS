FROM eclipse-temurin:21-jdk-alpine
COPY ./target/assignment2-0.0.1-SNAPSHOT.jar app.jar
COPY sensor.csv .
ENTRYPOINT ["java","-jar","/app.jar"]