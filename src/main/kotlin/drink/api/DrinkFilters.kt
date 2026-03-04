package de.okan.drink_and_snack_api.drink.api

import de.okan.drink_and_snack_api.drink.api.model.DrinkFiltersDTO
import de.okan.drink_and_snack_api.drink.api.model.DrinkPackagingDTO
import io.ktor.http.Parameters

fun String.toDrinkPackaging(): DrinkPackagingDTO? {
    return enumValues<DrinkPackagingDTO>().singleOrNull { this.equals(it.name, ignoreCase = true) }
}

fun Parameters.toDrinkFiltersDTO() = DrinkFiltersDTO(
    alcoholic = this["alcoholic"]?.toBoolean(),
    packaging = this["packaging"]?.toDrinkPackaging(),
)