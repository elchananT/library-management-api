plugins {
    alias(libs.plugins.kotlin.jvm)
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
