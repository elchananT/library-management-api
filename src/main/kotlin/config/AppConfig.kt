package com.polaris.config

data class AppConfig(
    val databaseUrl: String,
    val databaseUser: String,
    val databasePassword: String,
    val jwtSecret: String,
    val jwtIssuer: String,
    val jwtAudience: String,
    val jwtRealm: String,
    val jwtExpirationMs: Long,
) {
    companion object {
        fun fromEnv(): AppConfig {
            fun required(name: String): String =
                System.getenv(name)?.takeIf { it.isNotBlank() }
                ?: error("Missing required environment variable $name")

            // TODO: for production need to use the required
            return AppConfig(
                databaseUrl = "jdbc:postgresql://localhost:5435/library",
                databaseUser = "library",
                databasePassword = "library",
                jwtSecret = "secret",
                jwtIssuer = "issuer",
                jwtAudience = "audience",
                jwtRealm = "realm",
                jwtExpirationMs = 360000,
            )
        }
    }
}