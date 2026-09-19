plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(ktorLibs.plugins.ktor)
    application
}

group = "com.polaris"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(libs.logback.classic)
    implementation(ktorLibs.server.statusPages)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.runtime)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.auth)

    implementation(libs.postgresql)
    implementation(libs.hikari)

    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)

    implementation(libs.koin.ktor)

    implementation(libs.jbcrypt)
    implementation(libs.java.jwt)

    testImplementation(libs.koin.test)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.junit)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}

tasks.test {
    val env = System.getenv()
    environment(
        "DATABASE_URL" to (env["DATABASE_URL"] ?: "jdbc:postgresql://localhost:5435/library"),
        "DATABASE_USER" to (env["DATABASE_USER"] ?: "library"),
        "DATABASE_PASSWORD" to (env["DATABASE_PASSWORD"] ?: "library"),
        "JWT_SECRET" to (env["JWT_SECRET"] ?: "test-secret"),
        "JWT_ISSUER" to (env["JWT_ISSUER"] ?: "library-application"),
        "JWT_AUDIENCE" to (env["JWT_AUDIENCE"] ?: "readers"),
        "JWT_REALM" to (env["JWT_REALM"] ?: "library-application"),
        "JWT_EXPIRATION_MS" to (env["JWT_EXPIRATION_MS"] ?: "360000"),
    )
}
