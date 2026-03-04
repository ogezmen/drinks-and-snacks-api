package de.okan.drink_and_snack_api.drink.service

import de.okan.drink_and_snack_api.drink.api.model.CreateDrinkRequest
import de.okan.drink_and_snack_api.drink.api.model.DrinkDTO
import de.okan.drink_and_snack_api.drink.api.model.DrinkFiltersDTO
import java.util.UUID

/**
 * Application service responsible for managing drink-related use cases.
 */
interface DrinkService {

    /**
     * Retrieves all drinks associated to a store.
     *
     * @param storeId the ID of the associated store
     * @param filters optional for filtering by attributes
     *
     * @return a [List] of [DrinkDTO] representing all existing drinks.
     */
    fun getAllDrinks(storeId: UUID, filters: DrinkFiltersDTO? = null): List<DrinkDTO>

    /**
     * Retrieves a specific drink by its ID within a given store.
     *
     * @param id the unique identifier of the drink
     * @param storeId the unique identifier of the store
     *
     * @return the corresponding [DrinkDTO] if found, or `null` if no store exists with the given ID
     */
    fun getDrinkById(id: UUID, storeId: UUID): DrinkDTO?

    /**
     * Creates a new drink within the specified store.
     *
     * @param createDrinkRequest the data required to create the drink
     * @param storeId the unique identifier of the store
     *
     * @return the [DrinkDTO] representing the created drink
     */
    fun createDrink(createDrinkRequest: CreateDrinkRequest, storeId: UUID): DrinkDTO

    /**
     * Deletes a drink by its ID within a given store.
     *
     * @param id the unique identifier of the drink
     * @param storeId the unique identifier of the store
     */
    fun deleteDrink(id: UUID, storeId: UUID)
}