# BUILD
FROM eclipse-temurin:21-jdk-noble AS build
LABEL authors="lahiru.rajakaruna.dev"
WORKDIR "/app"

COPY mvnw .
COPY .mvn/ .mvn/
COPY src/ src/
COPY pom.xml .
COPY *.yaml .

RUN ["chmod", "755", "mvnw"]
RUN ["./mvnw", "install", "-DskipTests"]

# PACKAGE
FROM eclipse-temurin:21.0.12.1_1-jre AS package
WORKDIR "/app"
COPY --from=build /app/target/* .

ENTRYPOINT ["java","-jar","retail_manager.jar"]