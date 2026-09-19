package com.polaris.repositories.exposed

import com.polaris.db.UsersTable
import com.polaris.domain.User
import com.polaris.repositories.UserRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.ZoneOffset

class ExposedUserRepository : UserRepository {

    override fun findById(id: Long): User? = transaction {
        UsersTable.selectAll().where { UsersTable.id eq id }
            .map { it.toUser() }
            .singleOrNull()
    }

    override fun findByEmail(email: String): User? = transaction {
        UsersTable.selectAll().where { UsersTable.email.lowerCase() eq email.lowercase() }
            .map { it.toUser() }
            .singleOrNull()
    }

    override fun create(
        email: String,
        username: String,
        password: String
    ): User = transaction {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val id = UsersTable.insert {
            it[UsersTable.email] = email
            it[UsersTable.username] = username
            it[UsersTable.passwordHash] = password
            it[UsersTable.createdAt] = now
            it[UsersTable.updatedAt] = now
        } get UsersTable.id

        UsersTable.selectAll().where { UsersTable.id eq id }
            .map { it.toUser() }
            .single()
    }

    override fun existsByEmail(email: String): Boolean = transaction {
        UsersTable.selectAll().where { UsersTable.email.lowerCase() eq email.lowercase() }
            .limit(1)
            .any()
    }

    private fun ResultRow.toUser() = User(
        id = this[UsersTable.id].value,
        email = this[UsersTable.email],
        username = this[UsersTable.username],
        passwordHash = this[UsersTable.passwordHash],
        createdAt = this[UsersTable.createdAt].toInstant(ZoneOffset.UTC),
        updateAt = this[UsersTable.updatedAt].toInstant(ZoneOffset.UTC)
    )
}