FROM eclipse-temurin:21
ARG JAR_FILE=build/libs/*.jar
COPY ./build/libs/engine-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
