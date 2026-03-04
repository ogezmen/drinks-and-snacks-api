package de.okan.drink_and_snack_api.drink.persistence

import de.okan.drink_and_snack_api.drink.domain.Drink
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedDrinkRepository(
    private val database: Database
) : DrinkRepository {

    override fun findAll(storeId: UUID, filters: DrinkFilters?): List<Drink> = transaction(database) {
        DrinksTable.selectAll()
            .apply {
                andWhere { DrinksTable.storeId eq storeId }
                filters?.let { f ->
                    f.packaging?.let {
                        andWhere { DrinksTable.drinkPackaging eq it }
                    }
                    f.alcoholic?.let { alcoholic ->
                        if (alcoholic) {
                            andWhere { DrinksTable.alcoholPercentage neq 0.0 }
                        } else {
                            andWhere { DrinksTable.alcoholPercentage eq 0.0 }
                        }
                    }
                }
            }
            .map {
                Drink(
                    id = it[DrinksTable.id],
                    name = it[DrinksTable.name],
                    milliliters = it[DrinksTable.milliliters],
                    alcoholPercentage = it[DrinksTable.alcoholPercentage],
                    drinkPackaging = it[DrinksTable.drinkPackaging],
                    storeId = it[DrinksTable.storeId],
                )
            }
    }

    override fun findById(id: UUID, storeId: UUID): Drink? = transaction(database) {
        DrinksTable.select { DrinksTable.id eq id }.mapNotNull {
            Drink(
                id = it[DrinksTable.id],
                name = it[DrinksTable.name],
                milliliters = it[DrinksTable.milliliters],
                alcoholPercentage = it[DrinksTable.alcoholPercentage],
                drinkPackaging = it[DrinksTable.drinkPackaging],
                storeId = it[DrinksTable.storeId],
            )
        }.singleOrNull()
    }

    override fun create(drink: Drink, storeId: UUID): Drink = transaction(database) {
        DrinksTable.insert {
            it[id] = drink.id
            it[name] = drink.name
            it[milliliters] = drink.milliliters
            it[alcoholPercentage] = drink.alcoholPercentage
            it[drinkPackaging] = drink.drinkPackaging
            it[DrinksTable.storeId] = storeId
        }
        drink
    }

    override fun deleteById(id: UUID, storeId: UUID): Unit = transaction(database) {
        DrinksTable.deleteWhere { (DrinksTable.id eq id) and (DrinksTable.storeId eq storeId) }
    }
}