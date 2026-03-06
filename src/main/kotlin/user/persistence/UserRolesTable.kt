package de.okan.drinks_and_snacks_api.user.persistence

import de.okan.drinks_and_snacks_api.user.domain.Role
import org.jetbrains.exposed.sql.Table

object UserRolesTable : Table("user_roles") {
    val userId = uuid("user_id").references(UsersTable.id)
    val role = enumerationByName("role", 20, Role::class)

    override val primaryKey = PrimaryKey(userId, role)
}