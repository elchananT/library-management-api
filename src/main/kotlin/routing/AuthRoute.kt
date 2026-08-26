package com.polaris.routing

import com.polaris.dtos.AuthResponse
import com.polaris.dtos.LoginRequest
import com.polaris.dtos.RegisterRequest
import com.polaris.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.authRoute(userService: UserService) {
    post("/auth/register") {
        val request = call.receive<RegisterRequest>()
        val (user, token) = userService.register(request.email, request.username, request.password)

        call.respond(HttpStatusCode.Created, AuthResponse(token, user))
    }

    post("/auth/login") {
        val request = call.receive<LoginRequest>()
        val (user, token) = userService.login(request.email, request.password)

        call.respond(HttpStatusCode.OK, AuthResponse(token, user))
    }
}