package com.polaris

import com.polaris.routing.authRoute
import com.polaris.services.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService
) {
    routing {
        authRoute(userService)
    }
}