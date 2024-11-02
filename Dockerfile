FROM eclipse-temurin:21 AS build

# Set the working directory
WORKDIR /app

# Copy Gradle files
COPY build.gradle.kts settings.gradle.kts gradlew /app/
COPY gradle /app/gradle

# Download dependencies (this helps with caching)
RUN ./gradlew dependencies --no-daemon || return 0

# Copy the rest of the project files
COPY . /app

# Build the application
RUN ./gradlew bootJar -x test --no-daemon

# Start a new lightweight image for the final build
FROM eclipse-temurin:21

# Copy the built JAR file from the previous stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Expose the port that the application will run on
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
