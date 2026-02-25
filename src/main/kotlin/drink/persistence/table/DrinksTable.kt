package com.example.drink.persistence.table

import org.jetbrains.exposed.sql.Table

object DrinksTable : Table("drinks") {
    val id = uuid("id")
    val name = varchar("name", 255)

    override val primaryKey = PrimaryKey(id)
}