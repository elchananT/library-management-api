package com.polaris.routing

import com.polaris.db.BooksTable.author
import com.polaris.db.BooksTable.id
import com.polaris.db.BooksTable.isbn
import com.polaris.db.BooksTable.title
import com.polaris.domain.Book
import com.polaris.dtos.BookResponse
import com.polaris.dtos.CreateBookRequest
import com.polaris.dtos.UpdateBookRequest
import com.polaris.exceptions.ValidationException
import com.polaris.plugin.AUTH_JWT
import com.polaris.repositories.LoanRepository
import com.polaris.services.BookService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.bookRoute(bookService: BookService, loanRepository: LoanRepository) {
    fun Book.toResponse() = BookResponse(
        id = id,
        title = title,
        author = author,
        isbn = isbn,
        available = loanRepository.findActiveByBookId(id) == null
    )

    get("/books") {
        call.respond(bookService.list().map { it.toResponse() })
    }

    get("/books/{id}") {
        val id = call.pathId()
        call.respond(bookService.getById(id).toResponse())
    }

    authenticate(AUTH_JWT) {
        post("/books") {
            val request = call.receive<CreateBookRequest>()
            val book = bookService.create(request.title, request.author, request.isbn)
            call.respond(HttpStatusCode.Created,book.toResponse())
        }

        put("/books/{id}") {
            val id = call.pathId()
            val request = call.receive<UpdateBookRequest>()
            val book = bookService.update(id, request.title, request.author, request.isbn)
            call.respond(book.toResponse())
        }

        delete("/books/{id}") {
            val id = call.pathId()
            bookService.delete(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }


}

internal fun ApplicationCall.pathId(): Long =
    parameters["id"]?.toLongOrNull() ?: throw ValidationException("Invalid path id")