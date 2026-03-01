package de.okan.drink_and_snack_api.store.persistence

import de.okan.drink_and_snack_api.store.domain.Store
import java.util.UUID

/**
 * Repository for managing [Store] persistence.
 */
interface StoreRepository {

    /**
     * Retrieves every [Store]
     *
     * @return a [List] with every [Store]
     */
    fun findAll(): List<Store>

    /**
     * Retrieves a [Store] by ID
     *
     * @param id the unique identifier of the [Store]
     *
     * @return the [Store] if found, or `null` if no user exists with the given ID
     */
    fun findById(id: UUID): Store?

    /**
     * Creates a new [Store]
     *
     * @param store the [Store] to be created
     * @param ownerUserId the unique identifier of the store owner
     *
     * @return the created [Store]
     */
    fun create(store: Store, ownerUserId: UUID): Store

    /**
     * Deletes a [Store] by ID
     *
     * @param id the ID of the [Store]
     * @param ownerUserId the ID of the store owner
     */
    fun deleteById(id: UUID, ownerUserId: UUID)
}