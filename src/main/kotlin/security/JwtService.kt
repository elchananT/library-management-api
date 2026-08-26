package com.polaris.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.polaris.config.AppConfig
import com.polaris.domain.User
import java.util.Date

class JwtService(private val config: AppConfig) {
    private val algorithm = Algorithm.HMAC256(config.jwtSecret)
    private val verifier = JWT.require(algorithm)
        .withIssuer(config.jwtIssuer)
        .withAudience(config.jwtAudience)
        .build()

    fun generateToken(user: User): String {
        return JWT.create()
            .withIssuer(config.jwtIssuer)
            .withAudience(config.jwtAudience)
            .withClaim("userId", user.id)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + config.jwtExpirationMs))
            .sign(algorithm)
    }

    fun verifier(): JWTVerifier {
        return JWT.require(algorithm).withIssuer(config.jwtIssuer).withAudience(config.jwtAudience).build()
    }
}