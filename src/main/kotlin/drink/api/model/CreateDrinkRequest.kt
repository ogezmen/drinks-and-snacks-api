package de.okan.drink_and_snack_api.drink.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateDrinkRequest(
    val name: String,
    val milliliters: Int,
    val alcoholPercentage: Double,
    val packaging: DrinkPackagingDTO,
)
