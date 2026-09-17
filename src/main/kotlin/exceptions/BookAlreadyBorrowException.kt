package com.polaris.exceptions

class BookAlreadyBorrowException(bookId: Long) : RuntimeException("Book with $bookId is not found")