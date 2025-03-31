# Stage 1: Build Maven Project
FROM maven:3.8.6-openjdk-17-slim AS build
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies in a separate layer (this will be cached if pom.xml doesn't change)
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create a non-root user to run the application
RUN groupadd --system --gid 1001 appuser && \
    useradd --system --uid 1001 --gid 1001 appuser

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set ownership of the application files
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose the port your app runs on
EXPOSE 8080

# Run the application with optimized JVM settings
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]