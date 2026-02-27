package de.okan.drink_and_snack_api.store.service

import de.okan.drink_and_snack_api.store.api.model.CreateStoreRequest
import de.okan.drink_and_snack_api.store.service.model.StoreDTO
import java.util.UUID

interface StoreService {
    fun getAllStores(): List<StoreDTO>
    fun getStoreById(id: UUID): StoreDTO?
    fun createStore(store: CreateStoreRequest): StoreDTO
    fun deleteStore(id: UUID)
}