package de.okan.drink_and_snack_api.user.persistence

import de.okan.drink_and_snack_api.user.domain.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedUserRepository(
    private val database: Database,
) : UserRepository {

    override fun findById(id: UUID): User? = transaction(database) {
        UsersTable.select { UsersTable.id eq id }.mapNotNull {
            User(
                id = it[UsersTable.id],
                username = it[UsersTable.username],
                passwordHash = it[UsersTable.passwordHash],
                firstName = it[UsersTable.firstName],
                lastName = it[UsersTable.lastName],
            )
        }.singleOrNull()
    }

    override fun findByUsername(username: String): User? = transaction(database) {
        UsersTable.select { UsersTable.username eq username }.mapNotNull {
            User(
                id = it[UsersTable.id],
                username = it[UsersTable.username],
                passwordHash = it[UsersTable.passwordHash],
                firstName = it[UsersTable.firstName],
                lastName = it[UsersTable.lastName],
            )
        }.singleOrNull()
    }

    override fun create(user: User): User {
        transaction(database) {
            UsersTable.insert {
                it[id] = user.id
                it[username] = user.username
                it[passwordHash] = user.passwordHash
                it[firstName] = user.firstName
                it[lastName] = user.lastName
            }
        }
        return user
    }

    override fun deleteById(id: UUID) {
        transaction(database) {
            UsersTable.deleteWhere { UsersTable.id eq id }
        }
    }
}