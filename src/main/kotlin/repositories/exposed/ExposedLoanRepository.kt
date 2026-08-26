package com.polaris.repositories.exposed

import com.polaris.db.LoansTable
import com.polaris.domain.Loan
import com.polaris.repositories.LoanRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.time.ZoneOffset

class ExposedLoanRepository : LoanRepository {
    override fun findById(id: Long): Loan? = transaction {
        LoansTable.selectAll().where { LoansTable.id eq id }
            .map { it.toLoan() }
            .singleOrNull()
    }

    override fun findActiveByBookId(bookId: Long): Loan? = transaction {
        LoansTable.selectAll()
            .where { (LoansTable.bookId eq bookId) and (LoansTable.returnedAt.isNull()) }
            .map { it.toLoan() }
            .singleOrNull()
    }

    override fun findByUserId(userId: Long): List<Loan> = transaction {
        LoansTable.selectAll().where { LoansTable.userId eq userId }
            .orderBy(LoansTable.borrowedAt to SortOrder.DESC)
            .map { it.toLoan() }
    }

    override fun findAll(): List<Loan> = transaction {
        LoansTable.selectAll()
            .orderBy(LoansTable.borrowedAt to SortOrder.DESC)
            .map { it.toLoan() }
    }

    override fun create(userId: Long, bookId: Long): Loan = transaction {
        val id = LoansTable.insert {
            it[LoansTable.userId] = userId
            it[LoansTable.bookId] = bookId
            it[LoansTable.borrowedAt] = LocalDateTime.now()
            it[LoansTable.returnedAt] = null
        } get LoansTable.id

        LoansTable.selectAll().where { LoansTable.id eq id }
            .map { it.toLoan() }
            .single()
    }

    override fun markReturned(id: Long): Loan? = transaction {
        val updated = LoansTable.update({ LoansTable.id eq id }) {
            it[LoansTable.returnedAt] = LocalDateTime.now()
        }

        if (updated == 0) null
        else {
            LoansTable.selectAll().where { LoansTable.id eq id }
                .map { it.toLoan() }
                .single()
        }
    }

    private fun ResultRow.toLoan(): Loan = Loan(
        id = this[LoansTable.id].value,
        userId = this[LoansTable.userId].value,
        bookId = this[LoansTable.bookId].value,
        borrowedAt = this[LoansTable.borrowedAt].toInstant(ZoneOffset.UTC),
        returnedAt = this[LoansTable.returnedAt]?.toInstant(ZoneOffset.UTC)
    )
}