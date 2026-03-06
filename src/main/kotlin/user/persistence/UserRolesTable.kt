package de.okan.drink_and_snack_api.user.persistence

import de.okan.drink_and_snack_api.user.domain.Role
import org.jetbrains.exposed.sql.Table

object UserRolesTable : Table("user_roles") {
    val userId = uuid("user_id").references(UsersTable.id)
    val role = enumerationByName("role", 20, Role::class)

    override val primaryKey = PrimaryKey(userId, role)
}