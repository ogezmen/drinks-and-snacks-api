package de.okan.drinks_and_snacks_api.store.persistence

import de.okan.drinks_and_snacks_api.store.domain.Store
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
     *
     * @return the created [Store]
     */
    fun create(store: Store): Store

    /**
     * Deletes a [Store] by ID
     *
     * @param id the ID of the [Store]
     * @param ownerUserId the ID of the store owner
     */
    fun deleteById(id: UUID, ownerUserId: UUID)
}