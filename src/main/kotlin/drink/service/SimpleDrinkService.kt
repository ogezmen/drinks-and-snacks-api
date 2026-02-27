package com.example.drink.service

import com.example.drink.repository.DrinkRepository
import com.example.drink.repository.entity.Drink
import com.example.drink.service.mapper.toDTO
import com.example.drink.service.model.DrinkDTO
import java.util.UUID

class SimpleDrinkService(
    private val drinkRepository: DrinkRepository
) : DrinkService {

    override fun getAllDrinks(): List<DrinkDTO> = drinkRepository.findAll().map { it.toDTO() }

    override fun getDrinkById(id: UUID): DrinkDTO? = drinkRepository.findById(id)?.toDTO()

    override fun addDrink(drink: DrinkDTO): DrinkDTO {
        val drinkEntity = Drink(
            id = UUID.randomUUID(),
            name = drink.name
        )

        val savedEntity = drinkRepository.save(drinkEntity)
        return DrinkDTO(savedEntity.id, savedEntity.name)
    }

    override fun deleteDrink(id: UUID) {
        drinkRepository.deleteById(id)
    }
}
