package com.polaris.plugin

import com.polaris.config.AppConfig
import com.polaris.security.JwtService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.ApplicationConfig

const val AUTH_JWT = "auth-jwt"

fun Application.configureSecurity(config: AppConfig, jwtService: JwtService) {
    install(Authentication) {
        jwt(AUTH_JWT) {
            realm = config.jwtRealm
            verifier(jwtService.verifier())
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                if (userId != null) JWTPrincipal(credential.payload) else null
            }
        }
    }
}