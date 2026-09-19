package com.polaris.config

import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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

            fun optional(name: String): String? =
                System.getenv(name)?.takeIf { it.isNotBlank() }

            val rawUrl = required("DATABASE_URL")
            val database = parseDatabaseUrl(rawUrl, optional("DATABASE_USER"), optional("DATABASE_PASSWORD"))

            return AppConfig(
                databaseUrl = database.url,
                databaseUser = database.user,
                databasePassword = database.password,
                jwtSecret = required("JWT_SECRET"),
                jwtIssuer = required("JWT_ISSUER"),
                jwtAudience = required("JWT_AUDIENCE"),
                jwtRealm = required("JWT_REALM"),
                jwtExpirationMs = required("JWT_EXPIRATION_MS").toLong(),
            )
        }

        // Accepts both JDBC URLs (jdbc:postgresql://host:port/db) and the libpq-style
        // connection string Railway injects (postgres://user:pass@host:port/db).
        private fun parseDatabaseUrl(rawUrl: String, envUser: String?, envPassword: String?): DatabaseCredentials {
            if (rawUrl.startsWith("jdbc:")) {
                return DatabaseCredentials(
                    url = rawUrl,
                    user = envUser ?: error("Missing required environment variable DATABASE_USER"),
                    password = envPassword ?: error("Missing required environment variable DATABASE_PASSWORD"),
                )
            }

            val uri = URI(rawUrl)
            val userInfo = uri.userInfo?.let { decode(it) }
                ?: error("DATABASE_URL has no user credentials")
            val separator = userInfo.indexOf(':')
            val user = if (separator >= 0) userInfo.substring(0, separator) else userInfo
            val password = if (separator >= 0) userInfo.substring(separator + 1) else (envPassword ?: "")

            val host = uri.host ?: error("DATABASE_URL has no host")
            val port = if (uri.port != -1) uri.port else 5432
            val database = uri.path.removePrefix("/")

            return DatabaseCredentials(
                url = "jdbc:postgresql://$host:$port/$database",
                user = user,
                password = password,
            )
        }

        private fun decode(value: String): String =
            URLDecoder.decode(value, StandardCharsets.UTF_8)
    }

    private data class DatabaseCredentials(
        val url: String,
        val user: String,
        val password: String,
    )
}