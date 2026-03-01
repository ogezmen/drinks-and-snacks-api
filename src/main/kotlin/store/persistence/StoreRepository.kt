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
     * @return the [Store] if found, or `null` if no user exists with the given ID
     */
    fun findById(id: UUID): Store?

    /**
     * Creates a new [Store]
     *
     * @return the created [Store]
     */
    fun create(store: Store): Store

    /**
     * Deletes a [Store] by ID
     *
     * @param id the ID of the [Store]
     */
    fun deleteById(id: UUID)
}