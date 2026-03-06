package de.okan.drinks_and_snacks_api.drink.api.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class DrinkDTO(
    @Contextual val id: UUID,
    val name: String,
    val milliliters: Int,
    val alcoholPercentage: Double,
    val alcoholic: Boolean = alcoholPercentage > 0.0,
    val packaging: DrinkPackagingDTO,
) {
    init {
        require(alcoholPercentage in 0.0..100.0) {
            "Alcohol percentage must be between 0 and 100"
        }
    }
}