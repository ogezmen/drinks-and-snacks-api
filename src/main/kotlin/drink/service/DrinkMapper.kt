package de.okan.drink_and_snack_api.drink.service

import de.okan.drink_and_snack_api.drink.domain.Drink
import de.okan.drink_and_snack_api.drink.api.model.DrinkDTO

fun Drink.toDTO(): DrinkDTO = DrinkDTO(
    id = this.id,
    name = this.name
)