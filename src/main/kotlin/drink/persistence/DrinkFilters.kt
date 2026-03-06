package de.okan.drinks_and_snacks_api.drink.persistence

import de.okan.drinks_and_snacks_api.drink.domain.DrinkPackaging

data class DrinkFilters(
    val alcoholic: Boolean? = null,
    val packaging: DrinkPackaging? = null,
)