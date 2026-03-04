package drink.persistence

import configuration.setupTestDatabase
import de.okan.drink_and_snack_api.user.domain.User
import de.okan.drink_and_snack_api.user.persistence.ExposedUserRepository
import de.okan.drink_and_snack_api.drink.domain.Drink
import de.okan.drink_and_snack_api.drink.domain.DrinkPackaging
import de.okan.drink_and_snack_api.drink.persistence.DrinkFilters
import de.okan.drink_and_snack_api.drink.persistence.ExposedDrinkRepository
import de.okan.drink_and_snack_api.store.domain.Store
import de.okan.drink_and_snack_api.store.persistence.ExposedStoreRepository
import junit.framework.TestCase.assertTrue
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
            ownerUserId = user.id,
            ownerUsername = null,
        )
        storeRepository.create(store)
    }

    @Test
    fun `should find all drinks for a store`() {
        // Given
        val drink1 = Drink(
            id = UUID.randomUUID(),
            name = "Coke",
            storeId = storeId,
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.CAN,
        )
        val drink2 = Drink(
            id = UUID.randomUUID(),
            name = "Pepsi",
            storeId = storeId,
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.CAN,
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
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.CAN,
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
    fun `should filter drinks by alcoholic`() {
        // Given
        val coke = Drink(
            id = UUID.randomUUID(),
            name = "Coke",
            storeId = storeId,
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.CAN,
        )

        val beer = Drink(
            id = UUID.randomUUID(),
            name = "Beer",
            storeId = storeId,
            milliliters = 0,
            alcoholPercentage = 5.0,
            drinkPackaging = DrinkPackaging.BOTTLE,
        )

        repository.create(coke, storeId)
        repository.create(beer, storeId)

        // When
        val filterAlcoholic = DrinkFilters(
            alcoholic = true,
        )
        val filterNonAlcoholic = DrinkFilters(
            alcoholic = false,
        )
        val noFilter = DrinkFilters()

        val alcoholicDrinks = repository.findAll(storeId, filterAlcoholic)
        val nonAlcoholicDrinks = repository.findAll(storeId, filterNonAlcoholic)
        val allDrinks = repository.findAll(storeId, noFilter)

        // Then
        assertEquals(1, alcoholicDrinks.size)
        assertTrue(alcoholicDrinks[0].alcoholic)
        assertEquals("Beer", alcoholicDrinks[0].name)

        assertEquals(1, nonAlcoholicDrinks.size)
        assertFalse(nonAlcoholicDrinks[0].alcoholic)
        assertEquals("Coke", nonAlcoholicDrinks[0].name)

        assertEquals(2, allDrinks.size)
    }

    @Test
    fun `should only return drinks in a can`() {
        // Given
        val can = Drink(
            id = UUID.randomUUID(),
            name = "Coke",
            storeId = storeId,
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.CAN,
        )

        val bottle = Drink(
            id = UUID.randomUUID(),
            name = "Pepsi",
            storeId = storeId,
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.BOTTLE,
        )

        repository.create(can, storeId)
        repository.create(bottle, storeId)

        // When
        val filters = DrinkFilters(
            packaging = DrinkPackaging.CAN,
        )

        val drinks = repository.findAll(storeId, filters)

        // Then
        assertEquals(1, drinks.size)
        assertEquals(DrinkPackaging.CAN, drinks[0].drinkPackaging)
    }

    @Test
    fun `should delete drink`() {
        // Given
        val drink = Drink(
            id = UUID.randomUUID(),
            name = "Pepsi",
            storeId = storeId,
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.CAN,
        )
        repository.create(drink, storeId)

        // When
        repository.deleteById(drink.id, storeId)

        // Then
        val deletedDrink = repository.findById(drink.id, storeId)
        assertNull(deletedDrink)
    }
}