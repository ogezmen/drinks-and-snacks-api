package com.example.drink.persistence

import com.example.store.persistence.StoresTable
import org.jetbrains.exposed.sql.Table

object DrinksTable : Table("drinks") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val storeId = uuid("storeId")
        .references(StoresTable.id)

    override val primaryKey = PrimaryKey(id)
}