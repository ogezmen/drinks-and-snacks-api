package de.okan.drinks_and_snacks_api.drink.persistence

import de.okan.drinks_and_snacks_api.drink.domain.DrinkPackaging
import de.okan.drinks_and_snacks_api.store.persistence.StoresTable
import org.jetbrains.exposed.sql.Table

object DrinksTable : Table("drinks") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val milliliters = integer("milliliters")
    val alcoholPercentage = double("alcohol_percentage")
    val drinkPackaging = enumerationByName("packaging", 20, DrinkPackaging::class)
    val storeId = uuid("store_id")
        .references(StoresTable.id)

    override val primaryKey = PrimaryKey(id)
}