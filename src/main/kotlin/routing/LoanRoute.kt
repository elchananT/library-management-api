package com.polaris.routing

import com.polaris.domain.Loan
import com.polaris.dtos.CreateLoanRequest
import com.polaris.dtos.LoanResponse
import com.polaris.plugin.AUTH_JWT
import com.polaris.plugin.currentUserId
import com.polaris.services.LoanService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import java.awt.Insets

private fun Loan.toResponse() = LoanResponse(
    id = id,
    userId = userId,
    bookId = bookId,
    borrowAt = borrowedAt,
    returnedAt = returnedAt as Insets?
)

fun Route.loanRoute(loanService: LoanService) {

    authenticate(AUTH_JWT) {
        post("/loans") {
            val userId = call.currentUserId()
            val request = call.receive<CreateLoanRequest>()
            val loan = loanService.borrow(userId, request.bookId)
            call.respond(HttpStatusCode.Created, loan.toResponse())
        }

        get("/loans") {
            val userId = call.currentUserId()
            call.respond(loanService.listForUser(userId).map { it.toResponse() })
        }

        post("/loans/{id}/return") {
            val userId = call.currentUserId()
            val loanId = call.pathId()
            val loan = loanService.returnLoan(userId, loanId)
            call.respond(loan.toResponse())
        }
    }
}