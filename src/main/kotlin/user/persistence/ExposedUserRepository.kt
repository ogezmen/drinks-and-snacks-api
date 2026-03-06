package de.okan.drinks_and_snacks_api.user.persistence

import de.okan.drinks_and_snacks_api.user.domain.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedUserRepository(
    private val database: Database,
) : UserRepository {
    override fun findAll(): List<User> = transaction(database) {
        UsersTable
            .leftJoin(UserRolesTable)
            .selectAll()
            .groupBy { it[UsersTable.id] }
            .map { (_, rows) ->
                val row = rows.first()
                User(
                    id = row[UsersTable.id],
                    username = row[UsersTable.username],
                    passwordHash = row[UsersTable.passwordHash],
                    firstName = row[UsersTable.firstName],
                    lastName = row[UsersTable.lastName],
                    roles = rows.mapNotNull { it.getOrNull(UserRolesTable.role) }.toSet(),
                )
            }
    }

    private fun findByPredicate(predicate: Op<Boolean>): User? = transaction(database) {
        val rows = UsersTable
            .leftJoin(UserRolesTable)
            .select { predicate }
            .toList()

        if (rows.isEmpty()) {
            return@transaction null
        }

        val row = rows.first()

        return@transaction User(
            id = row[UsersTable.id],
            username = row[UsersTable.username],
            passwordHash = row[UsersTable.passwordHash],
            firstName = row[UsersTable.firstName],
            lastName = row[UsersTable.lastName],
            roles = rows.mapNotNull { it.getOrNull(UserRolesTable.role) }.toSet(),
        )
    }

    override fun findById(id: UUID): User? = transaction(database) {
        findByPredicate(UsersTable.id eq id)
    }

    override fun findByUsername(username: String): User? = transaction(database) {
        findByPredicate(UsersTable.username eq username)
    }

    override fun create(user: User): User = transaction(database) {
        UsersTable.insert {
            it[id] = user.id
            it[username] = user.username
            it[passwordHash] = user.passwordHash
            it[firstName] = user.firstName
            it[lastName] = user.lastName
        }

        user.roles.forEach { role ->
            UserRolesTable.insert {
                it[UserRolesTable.userId] = user.id
                it[UserRolesTable.role] = role
            }
        }

        return@transaction user
    }

    override fun deleteById(id: UUID) {
        transaction(database) {
            UsersTable.deleteWhere { UsersTable.id eq id }
        }
    }
}