package de.okan.drink_and_snack_api.drink.service

import de.okan.drink_and_snack_api.drink.api.model.CreateDrinkRequest
import de.okan.drink_and_snack_api.drink.api.model.DrinkDTO
import java.util.UUID

interface DrinkService {
    fun getAllDrinks(storeId: UUID): List<DrinkDTO>
    fun getDrinkById(id: UUID, storeId: UUID): DrinkDTO?
    fun createDrink(createDrinkRequest: CreateDrinkRequest, storeId: UUID): DrinkDTO
    fun deleteDrink(id: UUID, storeId: UUID)
}