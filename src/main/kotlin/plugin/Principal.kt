package com.polaris.plugin

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal

fun ApplicationCall.currentUserId(): Long {
    val principal = principal<JWTPrincipal>() ?: error("No JWT principal")
    return principal.payload.getClaim("userId").asLong()
}