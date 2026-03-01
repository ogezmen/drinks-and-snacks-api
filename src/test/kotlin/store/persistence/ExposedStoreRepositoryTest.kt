package store.persistence

import configuration.setupTestDatabase
import de.okan.drink_and_snack_api.auth.domain.User
import de.okan.drink_and_snack_api.auth.persistence.ExposedUserRepository
import de.okan.drink_and_snack_api.store.domain.Store
import de.okan.drink_and_snack_api.store.persistence.ExposedStoreRepository
import java.util.*
import kotlin.test.*

class ExposedStoreRepositoryTest {

    lateinit var repository: ExposedStoreRepository
    lateinit var user: User

    @BeforeTest
    fun setUp() {
        val database = setupTestDatabase()
        user = User(
            id = UUID.randomUUID(),
            username = "test",
            passwordHash = "test",
            firstName = "test",
            lastName = "test",
        )

        val userRepository = ExposedUserRepository(database)
        userRepository.create(user)

        repository = ExposedStoreRepository(database)
    }


    @Test
    fun `should save and retrieve store`()  {
        // Given
        val store = Store(
            id = UUID.randomUUID(),
            name = "Test Store",
            ownerUsername = user.username,
        )

        // When
        repository.create(store, user.id)
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
            ownerUsername = user.username,
        )
        val store2 = Store(
            id = UUID.randomUUID(),
            name = "Store 2",
            ownerUsername = user.username,
        )

        repository.create(store1, user.id)
        repository.create(store2, user.id)

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
            ownerUsername = user.username,
        )
        repository.create(store, user.id)

        // When
        repository.deleteById(store.id)
        val deletedStore = repository.findById(store.id)

        // Then
        assertNull(deletedStore)
    }
}