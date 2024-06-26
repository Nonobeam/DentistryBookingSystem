# Use an official OpenJDK 17 runtime as a parent image
FROM openjdk:17-oracle

# Create a directory for the application
WORKDIR /app

# Copy the JAR file into the container
COPY DentistryManagement.jar /app/DentistryManagement.jar

# Build argument for Google Cloud credentials
ARG GOOGLE_APPLICATION_CREDENTIALS_JSON

# Save the Google Cloud credentials JSON to a file
RUN echo $GOOGLE_APPLICATION_CREDENTIALS_JSON > /app/credentials.json

# Expose the port the application runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/DentistryManagement.jar"]
