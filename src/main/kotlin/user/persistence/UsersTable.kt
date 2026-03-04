package de.okan.drink_and_snack_api.user.persistence

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = uuid("id")
    val username = varchar("username", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)

    override val primaryKey = PrimaryKey(id)
}