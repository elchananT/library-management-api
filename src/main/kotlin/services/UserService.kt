package com.polaris.services

import com.polaris.domain.User
import com.polaris.dtos.UserResponse
import com.polaris.exceptions.InvalidCredentialException
import com.polaris.exceptions.ValidationException
import com.polaris.repositories.UserRepository
import com.polaris.security.JwtService
import org.mindrot.jbcrypt.BCrypt
import java.nio.file.attribute.UserPrincipalNotFoundException

class UserService(
    private val userRepo: UserRepository,
    private val jwtService: JwtService
) {

    fun register(email:String, username: String, password: String): Pair<UserResponse, String> {
        validateRequest(email, password)
        if (username.isBlank())
            throw ValidationException("A username is required")
        if (userRepo.existsByEmail(email))
            throw ValidationException("An account with that email is already registered")

        val user = userRepo.create(
            email,
            username,
            password = BCrypt.hashpw(password, BCrypt.gensalt()))

        val token = jwtService.generateToken(user)
        return toResponse(user) to token
    }

    fun login(email: String, password: String): Pair<UserResponse, String> {
        validateRequest(email, password)

        val user = userRepo.findByEmail(email)
            ?: throw InvalidCredentialException()
        if (!BCrypt.checkpw(password, user.passwordHash))
            throw InvalidCredentialException()

        val token = jwtService.generateToken(user)
        return toResponse(user) to token
    }

    fun getById(id: Long): UserResponse {
        val user = userRepo.findById(id)
            ?: throw UserPrincipalNotFoundException("User with id $id not found")

        return toResponse(user)
    }

    private fun validateRequest(email: String, password: String) {
        if (email.isBlank() || !email.contains("@"))
            throw ValidationException("A valid email is require")
        if (password.length < 8)
            throw ValidationException("A password must be at least 8 characters long")
    }

    private fun toResponse(user: User): UserResponse {
        return UserResponse(
            user.id,
            user.username,
            user.email,
            user.createdAt,
            user.updateAt
        )
    }
}