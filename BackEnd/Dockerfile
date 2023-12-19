# Use a base image with JDK
FROM openjdk:17

# Add a volume pointing to /tmp
VOLUME /tmp

# Add the application's jar file
ADD target/delivery-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]
