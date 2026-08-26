package com.polaris.domain

import java.time.Instant

data class User(
    val id: Long,
    val email: String,
    val username: String,
    val passwordHash: String,
    val createdAt: Instant,
    val updateAt: Instant
)

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val isbn: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)

data class Loan(
    val id: Long,
    val userId: Long,
    val bookId: Long,
    val borrowedAt: Instant,
    val returnedAt: Instant? = null,
) {
    val isActive: Boolean get() = returnedAt == null
}