package com.example.drink.service.mapper

import com.example.drink.repository.entity.Drink
import com.example.drink.service.model.DrinkDTO

fun Drink.toDTO(): DrinkDTO = DrinkDTO(
    id = this.id,
    name = this.name
)