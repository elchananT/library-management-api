package com.polaris.dtos

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    @Contextual val createdAt: Instant,
    @Contextual val updateAt: Instant
)

@Serializable
data class RegisterRequest(val email: String, val username: String, val password: String)

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class AuthResponse(val token: String, val user: UserResponse)

