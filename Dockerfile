# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Build the Ktor application using Gradle
# This runs inside the Docker image so the jar is always present
RUN --mount=type=cache,target=/root/.gradle \
    gradle --no-daemon build -x test 2>&1

# Copy the built application jar to the runtime image
COPY build/libs/Library-1.0.0-SNAPSHOT.jar app.jar

# The app reads all env vars at runtime from the environment:
#   DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD
#   JWT_SECRET, JWT_ISSUER, JWT_AUDIENCE, JWT_REALM, JWT_EXPIRATION_MS
#
# No ENV instructions — Railway injects these at deploy time via dashboard variables

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]