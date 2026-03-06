package de.okan.drinks_and_snacks_api.store.service

import de.okan.drinks_and_snacks_api.store.api.model.CreateStoreRequest
import de.okan.drinks_and_snacks_api.store.service.model.StoreDTO
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
     * @param ownerUserId the unique identifier of the store owner
     */
    fun deleteStore(id: UUID, ownerUserId: UUID)

    /**
     * Checks if user with given id is owner of store with given id
     *
     * @param ownerUserId the unique identifier of the user
     * @param storeId the unique identifier of the store
     *
     * @return `true` if the user is the owner store, `false` otherwise
     */
    fun isOwnerOfStore(ownerUserId: UUID, storeId: UUID): Boolean
}