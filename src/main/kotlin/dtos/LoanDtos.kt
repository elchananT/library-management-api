package com.polaris.dtos

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class CreateLoanRequest(val bookId: Long)

@Serializable
data class LoanResponse(
    val id: Long,
    val userId: Long,
    val bookId: Long,
    @Contextual val borrowedAt: Instant,
    @Contextual val returnedAt: Instant?,
)