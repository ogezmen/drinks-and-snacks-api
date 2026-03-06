package de.okan.drinks_and_snacks_api.drink.domain

import java.util.UUID

data class Drink(
    val id: UUID,
    val name: String,
    val storeId: UUID,
    val milliliters: Int,
    val alcoholPercentage: Double,
    val drinkPackaging: DrinkPackaging,
) {

    init {
        require(alcoholPercentage in 0.0..100.0) {
            "Alcohol percentage must be between 0 and 100"
        }
    }

    val alcoholic: Boolean
        get() = alcoholPercentage > 0.0
}