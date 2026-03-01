package drink.persistence

import configuration.setupTestDatabase
import de.okan.drink_and_snack_api.auth.domain.User
import de.okan.drink_and_snack_api.auth.persistence.ExposedUserRepository
import de.okan.drink_and_snack_api.drink.domain.Drink
import de.okan.drink_and_snack_api.drink.repository.ExposedDrinkRepository
import de.okan.drink_and_snack_api.store.domain.Store
import de.okan.drink_and_snack_api.store.persistence.ExposedStoreRepository
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ExposedDrinkRepositoryTest {

    private lateinit var repository: ExposedDrinkRepository
    private var storeId: UUID = UUID.randomUUID()

    @BeforeTest
    fun setupDatabase() {
        val database = setupTestDatabase()

        repository = ExposedDrinkRepository(database)

        val userRepository = ExposedUserRepository(database)
        val user = User(
            id = UUID.randomUUID(),
            username = "test",
            passwordHash = "test",
            firstName = "test",
            lastName = "test",
        )
        userRepository.create(user)

        val storeRepository = ExposedStoreRepository(database)
        val store = Store(
            id = storeId,
            name = "Test Store",
            ownerUsername = null,
        )
        storeRepository.create(store, user.id)
    }

    @Test
    fun `should find all drinks for a store`() {
        // Given
        val drink1 = Drink(
            id = UUID.randomUUID(),
            name = "Coke",
            storeId = storeId,
        )
        val drink2 = Drink(
            id = UUID.randomUUID(),
            name = "Pepsi",
            storeId = storeId,
        )
        repository.create(drink1, storeId)
        repository.create(drink2, storeId)

        // When
        val drinks = repository.findAll(storeId)

        // Then
        assertEquals(2, drinks.size)
        assertEquals("Coke", drinks[0].name)
        assertEquals("Pepsi", drinks[1].name)
    }

    @Test
    fun `should save and retrieve drink`() {
        // Given
        val drink = Drink(
            id = UUID.randomUUID(),
            name = "Coke",
            storeId = storeId,
        )

        // When
        repository.create(drink, storeId)

        // Then
        val savedDrink = repository.findById(drink.id, storeId)
        assertNotNull(savedDrink)
        assertEquals("Coke", savedDrink.name)
        assertEquals(storeId, savedDrink.storeId)
    }

    @Test
    fun `should delete drink`() {
        // Given
        val drink = Drink(
            id = UUID.randomUUID(),
            name = "Pepsi",
            storeId = storeId,
        )
        repository.create(drink, storeId)

        // When
        repository.deleteById(drink.id, storeId)

        // Then
        val deletedDrink = repository.findById(drink.id, storeId)
        assertNull(deletedDrink)
    }
}