package com.example.drink.repository

import com.example.drink.repository.entity.Drink
import java.util.UUID

interface DrinkRepository {
    fun findAll(storeId: UUID): List<Drink>
    fun findById(id: UUID, storeId: UUID): Drink?
    fun save(drink: Drink, storeId: UUID): Drink
    fun deleteById(id: UUID, storeId: UUID)
}