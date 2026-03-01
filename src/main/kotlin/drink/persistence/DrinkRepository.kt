package de.okan.drink_and_snack_api.drink.repository

import de.okan.drink_and_snack_api.drink.domain.Drink
import java.util.UUID

interface DrinkRepository {
    fun findAll(storeId: UUID): List<Drink>
    fun findById(id: UUID, storeId: UUID): Drink?
    fun create(drink: Drink, storeId: UUID): Drink
    fun deleteById(id: UUID, storeId: UUID)
}