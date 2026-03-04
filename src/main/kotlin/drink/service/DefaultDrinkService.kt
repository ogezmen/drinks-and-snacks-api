package de.okan.drink_and_snack_api.drink.service

import de.okan.drink_and_snack_api.drink.api.model.CreateDrinkRequest
import de.okan.drink_and_snack_api.drink.persistence.DrinkRepository
import de.okan.drink_and_snack_api.drink.domain.Drink
import de.okan.drink_and_snack_api.drink.api.model.DrinkDTO
import de.okan.drink_and_snack_api.drink.api.model.DrinkFiltersDTO
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
