package de.okan.drinks_and_snacks_api.store.service

import de.okan.drinks_and_snacks_api.store.api.model.CreateStoreRequest
import de.okan.drinks_and_snacks_api.store.domain.Store
import de.okan.drinks_and_snacks_api.store.persistence.StoreRepository
import de.okan.drinks_and_snacks_api.store.service.model.StoreDTO
import java.util.UUID

class DefaultStoreService(
    private val storeRepository: StoreRepository,
) : StoreService {
    override fun getAllStores(): List<StoreDTO> = storeRepository.findAll().map { it.toDTO() }

    override fun getStoreById(id: UUID): StoreDTO? = storeRepository.findById(id)?.toDTO()

    override fun createStore(createStoreRequest: CreateStoreRequest, ownerUserId: UUID): StoreDTO {
        val storeEntity = Store(
            id = UUID.randomUUID(),
            name = createStoreRequest.name,
            ownerUserId = ownerUserId,
            ownerUsername = null,
        )

        val savedStore = storeRepository.create(storeEntity)
        return savedStore.toDTO()
    }

    override fun deleteStore(id: UUID, ownerUserId: UUID) = storeRepository.deleteById(id, ownerUserId)

    override fun isOwnerOfStore(ownerUserId: UUID, storeId: UUID): Boolean = storeRepository.findById(storeId)?.ownerUserId == ownerUserId
}