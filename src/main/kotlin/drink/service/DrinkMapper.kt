package de.okan.drinks_and_snacks_api.drink.service

import de.okan.drinks_and_snacks_api.drink.domain.Drink
import de.okan.drinks_and_snacks_api.drink.api.model.DrinkDTO
import de.okan.drinks_and_snacks_api.drink.api.model.DrinkFiltersDTO
import de.okan.drinks_and_snacks_api.drink.api.model.DrinkPackagingDTO
import de.okan.drinks_and_snacks_api.drink.domain.DrinkPackaging
import de.okan.drinks_and_snacks_api.drink.persistence.DrinkFilters

fun Drink.toDTO(): DrinkDTO = DrinkDTO(
    id = id,
    name = name,
    milliliters = milliliters,
    alcoholPercentage = alcoholPercentage,
    packaging = drinkPackaging.toDTO()
)

fun DrinkFiltersDTO.toRepositoryFilters() = DrinkFilters(
    alcoholic = this.alcoholic,
    packaging = this.packaging?.toDomain(),
)

fun DrinkPackaging.toDTO(): DrinkPackagingDTO = when(this) {
    DrinkPackaging.BOTTLE -> DrinkPackagingDTO.BOTTLE
    DrinkPackaging.CAN -> DrinkPackagingDTO.CAN
}

fun DrinkPackagingDTO.toDomain(): DrinkPackaging = when(this) {
    DrinkPackagingDTO.BOTTLE -> DrinkPackaging.BOTTLE
    DrinkPackagingDTO.CAN -> DrinkPackaging.CAN
}