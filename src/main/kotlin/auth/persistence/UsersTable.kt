package de.okan.drink_and_snack_api.auth.persistence

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = uuid("id")
    val username = varchar("username", 255).uniqueIndex()
    val passwordHash = varchar("passwordHash", 255)
    val firstName = varchar("firstName", 255)
    val lastName = varchar("lastName", 255)

    override val primaryKey = PrimaryKey(id)
}