# Use an official OpenJDK 17 runtime as a parent image
FROM openjdk:17-oracle

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the host to the container
COPY DentistryManagement.jar /app/DentistryManagement.jar

# Build argument for the Google Cloud service account key file
ARG GOOGLE_APPLICATION_CREDENTIALS

# Save the credentials to a file inside the container
RUN echo "$GOOGLE_APPLICATION_CREDENTIALS" > /app/credentials.json

# Set the environment variable for Google Cloud credentials
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/credentials.json

# Run the application
ENTRYPOINT ["java", "-jar", "/app/DentistryManagement.jar"]
