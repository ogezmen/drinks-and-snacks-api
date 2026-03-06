package de.okan.drinks_and_snacks_api.drink.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateDrinkRequest(
    val name: String,
    val milliliters: Int,
    val alcoholPercentage: Double,
    val packaging: DrinkPackagingDTO,
)
