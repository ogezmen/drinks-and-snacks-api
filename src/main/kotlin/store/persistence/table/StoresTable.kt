package com.example.store.persistence.table

import org.jetbrains.exposed.sql.Table

object StoresTable : Table("stores") {
    val id = uuid("id")
    val name = varchar("name", 255)

    override val primaryKey = PrimaryKey(id)
}