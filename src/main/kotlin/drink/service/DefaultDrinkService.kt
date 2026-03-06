package de.okan.drinks_and_snacks_api.drink.service

import de.okan.drinks_and_snacks_api.drink.api.dto.CreateDrinkRequest
import de.okan.drinks_and_snacks_api.drink.persistence.DrinkRepository
import de.okan.drinks_and_snacks_api.drink.domain.Drink
import de.okan.drinks_and_snacks_api.drink.api.dto.DrinkDTO
import de.okan.drinks_and_snacks_api.drink.api.dto.DrinkFiltersDTO
import java.util.UUID

class DefaultDrinkService(
    private val drinkRepository: DrinkRepository
) : DrinkService {

    override fun getAllDrinks(storeId: UUID, filters: DrinkFiltersDTO?): List<DrinkDTO> {
        return drinkRepository.findAll(storeId, filters?.toRepositoryFilters()).map { it.toDTO() }
    }

    override fun getDrinkById(id: UUID, storeId: UUID): DrinkDTO? = drinkRepository.findById(id, storeId)?.toDTO()

    override fun createDrink(createDrinkRequest: CreateDrinkRequest, storeId: UUID): DrinkDTO {
        val drinkEntity = Drink(
            id = UUID.randomUUID(),
            name = createDrinkRequest.name,
            milliliters = createDrinkRequest.milliliters,
            alcoholPercentage = createDrinkRequest.alcoholPercentage,
            drinkPackaging = createDrinkRequest.packaging.toDomain(),
            storeId = storeId,
        )

        val savedEntity = drinkRepository.create(drinkEntity, storeId)
        return savedEntity.toDTO()
    }

    override fun deleteDrink(id: UUID, storeId: UUID) {
        drinkRepository.deleteById(id, storeId)
    }
}
