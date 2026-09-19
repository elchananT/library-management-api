FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradlew ./gradlew
COPY gradle ./gradle
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
COPY src ./src
RUN chmod +x ./gradlew && ./gradlew --no-daemon shadowJar -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/Library-all.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "exec java -jar app.jar -port=${PORT:-8080}"]