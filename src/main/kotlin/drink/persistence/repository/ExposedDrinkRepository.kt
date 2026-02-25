package com.example.drink.repository

import com.example.drink.persistence.table.DrinksTable
import com.example.drink.repository.entity.DrinkEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
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

    override fun findAll(): List<DrinkEntity> = transaction(database){
        DrinksTable.selectAll().map {
            DrinkEntity(
                id = it[DrinksTable.id],
                name = it[DrinksTable.name]
            )
        }
    }

    override fun findById(id: UUID): DrinkEntity? = transaction(database) {
        DrinksTable.select { DrinksTable.id eq id }.mapNotNull {
            DrinkEntity(
                id = it[DrinksTable.id],
                name = it[DrinksTable.name]
            )
        }.singleOrNull()
    }

    override fun save(drink: DrinkEntity): DrinkEntity = transaction(database) {
        DrinksTable.insert {
            it[id] = drink.id
            it[name] = drink.name
        }
        drink
    }

    override fun deleteById(id: UUID) {
        TODO("Not yet implemented")
    }
}