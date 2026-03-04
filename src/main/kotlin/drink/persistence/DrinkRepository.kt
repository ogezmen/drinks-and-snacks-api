package de.okan.drink_and_snack_api.drink.persistence

import de.okan.drink_and_snack_api.drink.domain.Drink
import java.util.UUID

/**
 * Repository for managing [Drink] persistence.
 */
interface DrinkRepository {

    /**
     * Retrieves every [Drink] associated to the given store ID
     *
     * @param storeId ID of the associated store
     * @param filters optional for filtering by attributes
     *
     * @return a [List] of every [Drink] associated to the store
     */
    fun findAll(storeId: UUID, filters: DrinkFilters? = null): List<Drink>

    /**
     * Retrieves a [Drink] by ID associated to the given store ID
     *
     * @param id the ID of the [Drink]
     * @param storeId ID of the associated store
     *
     * @return the [Drink] if found, or `null` if no user exists with the given ID and store ID
     */
    fun findById(id: UUID, storeId: UUID): Drink?

    /**
     * Creates a new [Drink]
     *
     * @param drink the [Drink] to be created
     * @param storeId ID of the associated store
     *
     * @return the created [Drink]
     */
    fun create(drink: Drink, storeId: UUID): Drink

    /**
     * Deletes a [Drink] by ID and store ID
     *
     * @param id the ID of the [Drink]
     * @param storeId ID of the associated store
     */
    fun deleteById(id: UUID, storeId: UUID)
}