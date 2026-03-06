package store.service

import de.okan.drinks_and_snacks_api.store.api.model.CreateStoreRequest
import de.okan.drinks_and_snacks_api.store.domain.Store
import de.okan.drinks_and_snacks_api.store.persistence.StoreRepository
import de.okan.drinks_and_snacks_api.store.service.DefaultStoreService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultStoreServiceTest {

    private val storeRepository = mockk<StoreRepository>()
    private val service = DefaultStoreService(storeRepository)

    @Test
    fun `should create a new store`() {
        // Given
        val createRequest = CreateStoreRequest(name = "Test Store")
        val storeEntity = Store(
            id = UUID.randomUUID(),
            name = createRequest.name,
            ownerUserId = UUID.randomUUID(),
            ownerUsername = null,
        )

        every { storeRepository.create(any()) } returns storeEntity

        // When
        val result = service.createStore(createRequest, UUID.randomUUID())

        // Then
        assertEquals(storeEntity.id, result.id)
        assertEquals(storeEntity.name, result.name)
    }

    @Test
    fun `should return all stores`() {
        // Given
        val store1 = Store(
            id = UUID.randomUUID(),
            name = "Store 1",
            ownerUserId = UUID.randomUUID(),
            ownerUsername = null
        )
        val store2 = Store(
            id = UUID.randomUUID(),
            name = "Store 2",
            ownerUserId = UUID.randomUUID(),
            ownerUsername = null
        )

        every { storeRepository.findAll() } returns listOf(store1, store2)

        // When
        val result = service.getAllStores()

        // Then
        assertEquals(2, result.size)
        assertEquals(store1.id, result[0].id)
        assertEquals(store1.name, result[0].name)
        assertEquals(store2.id, result[1].id)
        assertEquals(store2.name, result[1].name)
    }

    @Test
    fun `should return a store by id`() {
        // Given
        val storeId = UUID.randomUUID()
        val storeEntity = Store(
            id = storeId,
            name = "Test Store",
            ownerUserId = UUID.randomUUID(),
            ownerUsername = null,
        )

        every { storeRepository.findById(storeId) } returns storeEntity

        // When
        val result = service.getStoreById(storeId)

        // Then
        assertEquals(storeEntity.id, result?.id)
        assertEquals(storeEntity.name, result?.name)
    }

    @Test
    fun `should return null if store id does not exist`() {
        // Given
        val storeId = UUID.randomUUID()

        every { storeRepository.findById(storeId) } returns null

        // When
        val result = service.getStoreById(storeId)

        // Then
        assertEquals(null, result)
    }

    @Test
    fun `should delete a store by id`() {
        // Given
        val storeId = UUID.randomUUID()
        val ownerUserId = UUID.randomUUID()

        every { storeRepository.deleteById(storeId, ownerUserId) } returns Unit

        // When
        service.deleteStore(storeId, ownerUserId)

        // Then
        verify { storeRepository.deleteById(storeId, ownerUserId) }
    }

    @Test
    fun `should return true if user is store owner`() {
        // Given
        val storeId = UUID.randomUUID()
        val ownerUserId = UUID.randomUUID()

        val store = Store(
            id = storeId,
            name = "Test Store",
            ownerUserId = ownerUserId,
            ownerUsername = "Test User",
        )

        every { storeRepository.findById(any()) } returns store

        // When
        val isOwnerOfStore = service.isOwnerOfStore(ownerUserId, storeId)

        // Then
        assertTrue(isOwnerOfStore)
        verify { storeRepository.findById(storeId) }
    }

    @Test
    fun `should return false if user is not store owner`() {
        // Given
        val storeId = UUID.randomUUID()
        val ownerUserId = UUID.randomUUID()
        val otherUserId = UUID.randomUUID()

        val store = Store(
            id = storeId,
            name = "Test Store",
            ownerUserId = otherUserId,
            ownerUsername = "Test User",
        )

        every { storeRepository.findById(any()) } returns store

        // When
        val isOwnerOfStore = service.isOwnerOfStore(ownerUserId, storeId)

        // Then
        assertFalse(isOwnerOfStore)
        verify { storeRepository.findById(storeId) }
    }

    @Test
    fun `should return false if store isn't found`() {
        // Given
        val storeId = UUID.randomUUID()
        val ownerUserId = UUID.randomUUID()

        every { storeRepository.findById(any()) } returns null

        // When
        val isOwnerOfStore = service.isOwnerOfStore(ownerUserId, storeId)

        // Then
        assertFalse(isOwnerOfStore)
        verify { storeRepository.findById(storeId) }
    }
}