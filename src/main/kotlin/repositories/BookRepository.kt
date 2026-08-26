package com.polaris.repositories

import com.polaris.domain.Book

interface BookRepository {
    fun findById(id: Long): Book?
    fun findAll(): List<Book>
    fun findByIsbn(isbn: String): Book?
    fun create(title: String, author: String, isbn: String): Book
    fun update(id: Long, title: String, author: String, isbn: String): Book?
    fun delete(id: Long): Boolean
}