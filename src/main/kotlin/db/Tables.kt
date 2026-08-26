package com.polaris.db

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object UsersTable : LongIdTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255)
    val passwordHash = varchar("password_hash", 64)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

object BooksTable : LongIdTable("books") {
    val title = varchar("title", 500)
    val author = varchar("author", 255)
    val isbn = varchar("isbn", 20)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

object LoansTable : LongIdTable("loans") {
    val userId = reference("user_id", UsersTable.id)
    val bookId = reference("book_id", BooksTable.id)
    val borrowedAt = datetime("borrowed_at")
    val returnedAt = datetime("returned_at").nullable()
}