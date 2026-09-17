# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# The app reads all env vars at runtime from the environment:
#   DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD
#   JWT_SECRET, JWT_ISSUER, JWT_AUDIENCE, JWT_REALM, JWT_EXPIRATION_MS
#
# No ENV instructions are included here — Railway (or your runtime)
# injects these via the dashboard or `docker run -e`.

# Copy the built application jar
COPY build/libs/Library-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]