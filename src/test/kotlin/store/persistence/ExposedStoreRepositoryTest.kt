package store.persistence

import de.okan.drink_and_snack_api.store.domain.Store
import de.okan.drink_and_snack_api.store.persistence.ExposedStoreRepository
import org.jetbrains.exposed.sql.Database
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ExposedStoreRepositoryTest {

    private lateinit var repository: ExposedStoreRepository
    private lateinit var database: Database

    @BeforeTest
    fun setUp() {
        val uniqueDbName = "test_${UUID.randomUUID()}"
        database = Database.connect(
            url = "jdbc:h2:mem:$uniqueDbName;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver",
        )

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