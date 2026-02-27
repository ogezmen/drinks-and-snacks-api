package com.example.drink.repository

import com.example.drink.repository.entity.Drink
import java.util.UUID

interface DrinkRepository {
    fun findAll(): List<Drink>
    fun findById(id: UUID): Drink?
    fun save(drink: Drink): Drink
    fun deleteById(id: UUID)
}