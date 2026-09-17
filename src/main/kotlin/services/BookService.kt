package com.polaris.services

import com.polaris.domain.Book
import com.polaris.exceptions.DuplicateException
import com.polaris.exceptions.ValidationException
import com.polaris.repositories.BookRepository
import io.ktor.server.plugins.NotFoundException

class BookService(private val bookRepository: BookRepository) {
    fun list(): List<Book> = bookRepository.findAll()

    fun getById(id: Long): Book = (bookRepository.findById(id) ?: NotFoundException("Book with $id is not found")) as Book

    fun create(title: String, author: String, isbn: String): Book {
        validate(title, author, isbn)
        if (bookRepository.findByIsbn(isbn) != null) throw DuplicateException("Book with $isbn already exists")

        return bookRepository.create(title, author, isbn)
    }

    fun update(id: Long, title: String, author: String, isbn: String): Book {
        validate(title, author, isbn)
        val existingBook = bookRepository.findByIsbn(isbn)
        if (existingBook != null && existingBook.id != id) throw DuplicateException("Book with $id is not found")

        return bookRepository.update(id, title, author, isbn) ?: throw NotFoundException("Book with $id is not found")
    }

    fun delete(id: Long) {
        val deleteBook = bookRepository.delete(id)
        if (!deleteBook) throw NotFoundException("Book with $id is not found")
    }

    private fun validate(title: String, author: String, isbn: String) {
        if (title.isBlank()) throw ValidationException("Title is required")
        if (author.isBlank()) throw ValidationException("Author is required")
        if (isbn.isBlank()) throw ValidationException("Isbn is required")
    }
}