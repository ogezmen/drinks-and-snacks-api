package com.example.drink.service

import com.example.drink.api.model.DrinkDTO
import java.util.UUID

interface DrinkService {
    fun getAllDrinks(storeId: UUID): List<DrinkDTO>
    fun getDrinkById(id: UUID, storeId: UUID): DrinkDTO?
    fun addDrink(drink: DrinkDTO, storeId: UUID): DrinkDTO
    fun deleteDrink(id: UUID, storeId: UUID)
}