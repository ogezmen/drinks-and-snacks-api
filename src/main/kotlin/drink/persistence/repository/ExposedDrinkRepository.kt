package com.example.drink.repository

import com.example.drink.persistence.table.DrinksTable
import com.example.drink.repository.entity.Drink
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedDrinkRepository(
    private val database: Database
) : DrinkRepository {

    init {
        transaction(database) {
            SchemaUtils.create(DrinksTable)
        }
    }

    override fun findAll(): List<Drink> = transaction(database) {
        DrinksTable.selectAll().map {
            Drink(
                id = it[DrinksTable.id],
                name = it[DrinksTable.name]
            )
        }
    }

    override fun findById(id: UUID): Drink? = transaction(database) {
        DrinksTable.select { DrinksTable.id eq id }.mapNotNull {
            Drink(
                id = it[DrinksTable.id],
                name = it[DrinksTable.name]
            )
        }.singleOrNull()
    }

    override fun save(drink: Drink): Drink = transaction(database) {
        DrinksTable.insert {
            it[id] = drink.id
            it[name] = drink.name
        }
        drink
    }

    override fun deleteById(id: UUID) {
        DrinksTable.deleteWhere { DrinksTable.id eq id }
    }
}