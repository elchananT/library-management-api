package com.polaris.config

import com.polaris.repositories.BookRepository
import com.polaris.repositories.LoanRepository
import com.polaris.repositories.UserRepository
import com.polaris.repositories.exposed.ExposedBookRepository
import com.polaris.repositories.exposed.ExposedLoanRepository
import com.polaris.repositories.exposed.ExposedUserRepository
import com.polaris.security.JwtService
import com.polaris.services.BookService
import com.polaris.services.LoanService
import com.polaris.services.UserService
import org.koin.dsl.module

fun appModule(config: AppConfig) = module {
    single { config }
    single { JwtService(get()) }

    single<UserRepository> { ExposedUserRepository() }
    single<BookRepository> { ExposedBookRepository() }
    single<LoanRepository> { ExposedLoanRepository() }

    single { UserService(get<UserRepository>(), get<JwtService>()) }
    single { BookService(get<BookRepository>()) }
    single { LoanService(get<LoanRepository>(), get<BookRepository>(), get<UserRepository>()) }
}