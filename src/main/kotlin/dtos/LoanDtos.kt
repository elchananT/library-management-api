package com.polaris.dtos

import kotlinx.serialization.Serializable
import java.awt.Insets
import java.time.Instant

@Serializable
data class CreateLoanRequest(val bookId: Long)

@Serializable
data class LoanResponse(
    val id: Long,
    val userId: Long,
    val bookId: Long,
    val borrowAt: Instant,
    val returnedAt: Insets?,
)