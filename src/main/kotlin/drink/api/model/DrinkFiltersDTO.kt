package de.okan.drinks_and_snacks_api.drink.api.model

data class DrinkFiltersDTO(
    val alcoholic: Boolean? = null,
    val packaging: DrinkPackagingDTO? = null,
)
