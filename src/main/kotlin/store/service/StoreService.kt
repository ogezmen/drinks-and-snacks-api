package de.okan.drink_and_snack_api.store.service

import de.okan.drink_and_snack_api.store.api.model.CreateStoreRequest
import de.okan.drink_and_snack_api.store.service.model.StoreDTO
import java.util.UUID

/**
 * Application service responsible for managing store-related use cases.
 */
interface StoreService {

    /**
     * Retrieves all stores.
     *
     * @return a [List] of [StoreDTO] representing all existing stores.
     */
    fun getAllStores(): List<StoreDTO>

    /**
     * Retrieves a store by its unique identifier.
     *
     * @param id the unique identifier of the store
     *
     * @return the corresponding [StoreDTO] if found, or `null` if no store exists with the given ID
     */
    fun getStoreById(id: UUID): StoreDTO?

    /**
     * Creates a new store.
     *
     * @param createStoreRequest the data required to create the store
     * @param ownerUserId the unique identifier of the store owner
     *
     * @return the [StoreDTO] representing the created store
     */
    fun createStore(createStoreRequest: CreateStoreRequest, ownerUserId: UUID): StoreDTO

    /**
     * Deletes a store by its unique identifier.
     *
     * @param id the unique identifier of the store to delete
     */
    fun deleteStore(id: UUID)
}