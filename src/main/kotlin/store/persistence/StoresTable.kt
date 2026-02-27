package de.okan.drink_and_snack_api.store.persistence

import org.jetbrains.exposed.sql.Table

object StoresTable : Table("stores") {
    val id = uuid("id")
    val name = varchar("name", 255)

    override val primaryKey = PrimaryKey(id)
}