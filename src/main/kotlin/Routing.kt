package com.polaris

import com.polaris.repositories.LoanRepository
import com.polaris.routing.authRoute
import com.polaris.routing.bookRoute
import com.polaris.routing.loanRoute
import com.polaris.services.BookService
import com.polaris.services.LoanService
import com.polaris.services.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService,
    bookService: BookService,
    loanRepository: LoanRepository,
    loanService: LoanService
) {
    routing {
        authRoute(userService)
        bookRoute(bookService, loanRepository)
        loanRoute(loanService)
    }
}