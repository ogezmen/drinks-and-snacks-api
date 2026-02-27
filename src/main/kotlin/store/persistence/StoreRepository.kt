package de.okan.drink_and_snack_api.store.persistence

import de.okan.drink_and_snack_api.store.domain.Store
import java.util.UUID

interface StoreRepository {
    fun findAll(): List<Store>
    fun findById(id: UUID): Store?
    fun save(store: Store): Store
    fun deleteById(id: UUID)
}