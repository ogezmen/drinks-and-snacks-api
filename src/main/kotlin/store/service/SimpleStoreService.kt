package com.example.store.service

import com.example.store.api.model.CreateStoreRequest
import com.example.store.domain.Store
import com.example.store.persistence.StoreRepository
import com.example.store.service.model.StoreDTO
import java.util.UUID

class SimpleStoreService(
    private val storeRepository: StoreRepository,
) : StoreService {
    override fun getAllStores(): List<StoreDTO> = storeRepository.findAll().map{ StoreDTO(id = it.id, name = it.name) }

    override fun getStoreById(id: UUID): StoreDTO? = storeRepository.findById(id)?.let { StoreDTO(id = it.id, name = it.name) }

    override fun createStore(store: CreateStoreRequest): StoreDTO {
        val storeEntity = Store(
            id = UUID.randomUUID(),
            name = store.name
        )

        val savedStore = storeRepository.save(storeEntity)
        return savedStore.toDTO()
    }

    override fun deleteStore(id: UUID) = storeRepository.deleteById(id)
}