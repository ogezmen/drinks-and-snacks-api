package com.example.drink.service

import com.example.drink.domain.Drink
import com.example.drink.api.model.DrinkDTO

fun Drink.toDTO(): DrinkDTO = DrinkDTO(
    id = this.id,
    name = this.name
)