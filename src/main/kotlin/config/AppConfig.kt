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

            return AppConfig(
                databaseUrl = required("DATABASE_URL"),
                databaseUser = required("DATABASE_USER"),
                databasePassword = required("DATABASE_PASSWORD"),
                jwtSecret = required("JWT_SECRET"),
                jwtIssuer = required("JWT_ISSUER"),
                jwtAudience = required("JWT_AUDIENCE"),
                jwtRealm = required("JWT_REALM"),
                jwtExpirationMs = required("JWT_EXPIRATION_MS").toLong(),
            )
        }
    }
}