package com.polaris.services

import com.polaris.domain.Loan
import com.polaris.exceptions.BookAlreadyBorrowException
import com.polaris.exceptions.LoanReturnedException
import com.polaris.exceptions.UnauthorizedOperationException
import com.polaris.repositories.BookRepository
import com.polaris.repositories.LoanRepository
import com.polaris.repositories.UserRepository
import io.ktor.server.plugins.NotFoundException

class LoanService(
    private val loanRepository: LoanRepository,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository
) {
    fun borrow(userId: Long, bookId: Long): Loan {
        userRepository.findById(userId) ?: throw NotFoundException("User with $userId is not found")
        bookRepository.findById(bookId) ?: throw NotFoundException("Book with $bookId is not found")

        if (loanRepository.findActiveByBookId(bookId) != null) throw BookAlreadyBorrowException(bookId)

        return loanRepository.create(userId, bookId)
    }

    fun returnLoan(loanId: Long, userId: Long): Loan {
        val loan = loanRepository.findById(loanId) ?: throw NotFoundException("User with $loanId is not found")
        if (loan.userId != userId) throw UnauthorizedOperationException("You cannot return a book that you account dose not own")
        if (!loan.isActive) throw LoanReturnedException("Book was already returned")
        return loanRepository.markReturned(loanId) ?: throw NotFoundException("Book was marked as returned")
    }

    fun findById(loanId: Long): Loan = loanRepository.findById(loanId) ?: throw NotFoundException("User with $loanId is not found")

    fun listForUser(userId: Long): List<Loan> {
        userRepository.findById(userId) ?: throw NotFoundException("User with $userId is not found")
        return loanRepository.findByUserId(userId)
    }

    fun list() = loanRepository.findAll()
}