package com.polaris.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CreateBookRequest(val title: String, val author: String, val isbn: String)

@Serializable
data class UpdateBookRequest(val id: Long, val title: String, val author: String, val isbn: String)

@Serializable
data class BookResponse(
    val id: Long,
    val title: String,
    val author: String,
    val isbn: String,
    val available: Boolean,
)