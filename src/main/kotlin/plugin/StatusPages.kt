package com.polaris.plugin

import com.polaris.exceptions.InvalidCredentialException
import com.polaris.exceptions.ValidationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import java.sql.SQLException


fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<SQLException> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Unknown error")
        }

        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Unknown error")
        }

        exception<ValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Unknown error")
        }

        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.message ?: "Unknown error")
        }

        exception<InvalidCredentialException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message ?: "Unknown error")
        }

        exception<RuntimeException> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Unknown error")
        }
    }
}

/*private fun statusFor(cause: Exception): HttpStatusCode {
    return when (cause) {
        is SQLException, is InvalidCredentialException, is ValidationException, is InvalidCredentialException -> HttpStatusCode.BadRequest
        is NotFoundException -> HttpStatusCode.NotFound
        else -> HttpStatusCode.InternalServerError
    }
}*/

