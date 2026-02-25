package com.example.drink.service

import com.example.drink.service.model.DrinkDTO
import java.util.UUID

interface DrinkService {
    fun getAllDrinks(): List<DrinkDTO>
    fun getDrinkById(id: UUID): DrinkDTO?
    fun addDrink(drink: DrinkDTO): DrinkDTO
}