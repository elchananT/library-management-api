FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copy the built jar from gradle build
COPY build/libs/Library-1.0.0-SNAPSHOT.jar app.jar

# Minimal env vars for Ktor + JWT to run
# DATABASE_URL etc. should be provided at runtime via .env or docker run -e
ENV JWT_SECRET=482d87b7c87bf15ba194048bae7a18556c831f94b157cbddc9cbc09ba211469c \
    JWT_ISSUER=library-application \
    JWT_AUDIENCE=readers \
    JWT_REALM=library-application \
    JWT_EXPIRATION_MS=360000

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]