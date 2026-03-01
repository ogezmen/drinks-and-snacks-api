package store.persistence

import configuration.setupTestDatabase
import de.okan.drink_and_snack_api.store.domain.Store
import de.okan.drink_and_snack_api.store.persistence.ExposedStoreRepository
import java.util.*
import kotlin.test.*

class ExposedStoreRepositoryTest {

    private lateinit var repository: ExposedStoreRepository

    @BeforeTest
    fun setUp() {
        val database = setupTestDatabase()
        repository = ExposedStoreRepository(database)
    }


    @Test
    fun `should save and retrieve store`()  {
        // Given
        val store = Store(
            id = UUID.randomUUID(),
            name = "Test Store",
        )

        // When
        repository.create(store)
        val retrievedStore = repository.findById(store.id)

        // Then
        assertNotNull(retrievedStore)
        assertEquals(store.name, retrievedStore.name)
    }

    @Test
    fun `should return all stores`() {
        // Given
        val store1 = Store(
            id = UUID.randomUUID(),
            name = "Store 1",
        )
        val store2 = Store(
            id = UUID.randomUUID(),
            name = "Store 2",
        )
        repository.create(store1)
        repository.create(store2)

        // When
        val stores = repository.findAll()

        // Then
        assertEquals(2, stores.size)
        assertEquals("Store 1", stores[0].name)
        assertEquals("Store 2", stores[1].name)
    }

    @Test
    fun `should delete store`() {
        // Given
        val store = Store(
            id = UUID.randomUUID(),
            name = "Test Store",
        )
        repository.create(store)

        // When
        repository.deleteById(store.id)
        val deletedStore = repository.findById(store.id)

        // Then
        assertNull(deletedStore)
    }
}