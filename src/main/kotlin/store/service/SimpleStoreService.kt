package de.okan.drink_and_snack_api.store.service

import de.okan.drink_and_snack_api.store.api.model.CreateStoreRequest
import de.okan.drink_and_snack_api.store.domain.Store
import de.okan.drink_and_snack_api.store.persistence.StoreRepository
import de.okan.drink_and_snack_api.store.service.model.StoreDTO
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