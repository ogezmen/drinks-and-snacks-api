package com.example.drink.service

import com.example.drink.repository.DrinkRepository
import com.example.drink.repository.entity.DrinkEntity
import com.example.drink.service.model.DrinkDTO
import java.util.UUID

class SimpleDrinkService(
    private val drinkRepository: DrinkRepository
) : DrinkService {

    override fun getAllDrinks(): List<DrinkDTO> = drinkRepository
        .findAll()
        .map { DrinkDTO(it.id, it.name) }

    override fun getDrinkById(id: UUID): DrinkDTO? = drinkRepository
        .findById(id)
        ?.let { DrinkDTO(it.id, it.name) }

    override fun addDrink(drink: DrinkDTO): DrinkDTO {
        val drinkEntity = DrinkEntity(
            id = UUID.randomUUID(),
            name = drink.name
        )

        val savedEntity = drinkRepository.save(drinkEntity)
        return DrinkDTO(savedEntity.id, savedEntity.name)
    }
}
