package de.okan.drink_and_snack_api.drink.persistence

import de.okan.drink_and_snack_api.drink.domain.DrinkPackaging

data class DrinkFilters(
    val alcoholic: Boolean? = null,
    val packaging: DrinkPackaging? = null,
)