package de.okan.drink_and_snack_api.drink.api.model

data class DrinkFiltersDTO(
    val alcoholic: Boolean? = null,
    val packagingDTO: DrinkPackagingDTO? = null,
)
