# Stage 1: Build with Maven
FROM maven:3.8.4-openjdk-17 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17
COPY --from=build /usr/src/app/target/delivery-0.0.1-SNAPSHOT.jar app.jar
VOLUME /tmp
ENTRYPOINT ["java","-jar","/app.jar"]
