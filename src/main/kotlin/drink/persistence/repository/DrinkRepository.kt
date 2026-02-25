package com.example.drink.repository

import com.example.drink.repository.entity.DrinkEntity
import java.util.UUID

interface DrinkRepository {
    fun findAll(): List<DrinkEntity>
    fun findById(id: UUID): DrinkEntity?
    fun save(drink: DrinkEntity): DrinkEntity
    fun deleteById(id: UUID)
}