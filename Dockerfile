# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY out/artifacts/springMVC_json_view_jar/springMVC_json_view.jar app.jar

# Expose the application port
EXPOSE 8181

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
