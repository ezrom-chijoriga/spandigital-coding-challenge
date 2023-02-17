# Maven
FROM maven:3.8.1-openjdk-15-slim AS build
WORKDIR /app
COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn package

# Java
FROM openjdk:15-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar /opt/app.jar
COPY ./entrypoint.sh /opt/entrypoint.sh

ENTRYPOINT ["/opt/entrypoint.sh"]
