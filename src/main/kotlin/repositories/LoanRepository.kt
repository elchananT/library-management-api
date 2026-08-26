package com.polaris.repositories

import com.polaris.domain.Loan
import com.polaris.domain.User

interface LoanRepository {
    fun findById(id: Long): Loan?
    fun findActiveByBookId(bookId: Long): Loan?
    fun findByUserId(userId: Long): List<Loan>
    fun findAll(): List<Loan>
    fun create(userId: Long, bookId: Long): Loan
    fun markReturned(id: Long): Loan?
}