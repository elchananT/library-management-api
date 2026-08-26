package com.polaris.repositories.exposed

import com.polaris.db.BooksTable
import com.polaris.domain.Book
import com.polaris.repositories.BookRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.time.ZoneOffset

class ExposedBookRepository : BookRepository {
    override fun findById(id: Long): Book? = transaction {
       BooksTable.selectAll().where { BooksTable.id eq id }
           .map { it.toBook() }
            .singleOrNull()
    }

    override fun findAll(): List<Book> = transaction {
        BooksTable.selectAll()
            .orderBy(BooksTable.id)
            .map { it.toBook() }
    }

    override fun findByIsbn(isbn: String): Book? = transaction {
        BooksTable.selectAll().where { BooksTable.isbn eq isbn }
            .map { it.toBook() }
            .singleOrNull()
    }

    override fun create(title: String, author: String, isbn: String): Book = transaction {
        val now = LocalDateTime.now()
        val id = BooksTable.insert {
            it[BooksTable.title] = title
            it[BooksTable.author] = author
            it[BooksTable.isbn] = isbn
            it[BooksTable.createdAt] = now
            it[BooksTable.updatedAt] = now
        } get BooksTable.id

        BooksTable.selectAll().where { BooksTable.id eq id }
            .map { it.toBook() }
            .single()
    }

    override fun update(
        id: Long,
        title: String,
        author: String,
        isbn: String
    ): Book? = transaction {
        val updated = BooksTable.update({ BooksTable.id eq id }) {
            it[BooksTable.title] = title
            it[BooksTable.author] = author
            it[BooksTable.isbn] = isbn
            it[BooksTable.updatedAt] = LocalDateTime.now()
        }

        if (updated == 0) null
        else {
            BooksTable.selectAll().where { BooksTable.id eq id }
                .map { it.toBook() }
                .single()
        }
    }

    override fun delete(id: Long): Boolean {
        return BooksTable.deleteWhere { BooksTable.id eq id } > 0
    }

    private fun ResultRow.toBook(): Book = Book(
        id = this[BooksTable.id].value,
        title = this[BooksTable.title],
        author = this[BooksTable.author],
        isbn = this[BooksTable.isbn],
        createdAt = this[BooksTable.createdAt].toInstant(ZoneOffset.UTC),
        updatedAt = this[BooksTable.updatedAt].toInstant(ZoneOffset.UTC),
    )
}