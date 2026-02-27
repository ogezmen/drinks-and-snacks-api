package com.example.drink.repository

import com.example.drink.persistence.DrinksTable
import com.example.drink.domain.Drink
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
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

    override fun findAll(storeId: UUID): List<Drink> = transaction(database) {
        DrinksTable.select { DrinksTable.storeId eq storeId }.map {
            Drink(
                id = it[DrinksTable.id],
                name = it[DrinksTable.name],
                storeId = it[DrinksTable.storeId],
            )
        }
    }

    override fun findById(id: UUID, storeId: UUID): Drink? = transaction(database) {
        DrinksTable.select { DrinksTable.id eq id }.mapNotNull {
            Drink(
                id = it[DrinksTable.id],
                name = it[DrinksTable.name],
                storeId = it[DrinksTable.storeId],
            )
        }.singleOrNull()
    }

    override fun save(drink: Drink, storeId: UUID): Drink = transaction(database) {
        DrinksTable.insert {
            it[id] = drink.id
            it[name] = drink.name
            it[DrinksTable.storeId] = storeId
        }
        drink
    }

    override fun deleteById(id: UUID, storeId: UUID) {
        DrinksTable.deleteWhere { (DrinksTable.id eq id) and (DrinksTable.storeId eq storeId) }
    }
}