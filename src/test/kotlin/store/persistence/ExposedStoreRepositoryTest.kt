package store.persistence

import configuration.setupTestDatabase
import de.okan.drinks_and_snacks_api.user.domain.User
import de.okan.drinks_and_snacks_api.user.persistence.ExposedUserRepository
import de.okan.drinks_and_snacks_api.store.domain.Store
import de.okan.drinks_and_snacks_api.store.persistence.ExposedStoreRepository
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
            roles = setOf(),
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
            ownerUserId = user.id,
            ownerUsername = user.username,
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
            ownerUserId = user.id,
            ownerUsername = user.username,
        )
        val store2 = Store(
            id = UUID.randomUUID(),
            name = "Store 2",
            ownerUserId = user.id,
            ownerUsername = user.username,
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
            ownerUserId = user.id,
            ownerUsername = user.username,
        )
        repository.create(store)

        // When
        repository.deleteById(store.id, user.id)
        val deletedStore = repository.findById(store.id)

        // Then
        assertNull(deletedStore)
    }
}