package com.polaris.repositories

import com.polaris.domain.User

interface UserRepository {
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
    fun create(email: String, username: String, password: String): User
    fun existsByEmail(email: String): Boolean
}