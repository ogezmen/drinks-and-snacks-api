package de.okan.drink_and_snack_api.store.persistence

import de.okan.drink_and_snack_api.auth.persistence.UsersTable
import org.jetbrains.exposed.sql.Table

object StoresTable : Table("stores") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val ownerUserId = uuid("owner_user_id").references(UsersTable.id)

    override val primaryKey = PrimaryKey(id)
}