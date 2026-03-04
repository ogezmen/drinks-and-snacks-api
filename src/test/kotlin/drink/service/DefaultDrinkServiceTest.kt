package drink.service

import de.okan.drink_and_snack_api.drink.api.model.CreateDrinkRequest
import de.okan.drink_and_snack_api.drink.api.model.DrinkPackagingDTO
import de.okan.drink_and_snack_api.drink.domain.Drink
import de.okan.drink_and_snack_api.drink.domain.DrinkPackaging
import de.okan.drink_and_snack_api.drink.persistence.DrinkRepository
import de.okan.drink_and_snack_api.drink.service.DefaultDrinkService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultDrinkServiceTest {

    private val drinkRepository = mockk<DrinkRepository>()
    private val service = DefaultDrinkService(drinkRepository)

    @Test
    fun `should return all drinks`() {
        // Given
        val storeId = UUID.randomUUID()
        val drink1 = Drink(
            id = UUID.randomUUID(),
            name = "Coca-Cola",
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.CAN,
            storeId = storeId,
        )
        val drink2 = Drink(
            id = UUID.randomUUID(),
            name = "Pepsi",
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.CAN,
            storeId = storeId,
        )

        every { drinkRepository.findAll(any()) } returns listOf(drink1, drink2)

        // When
        val drinks = service.getAllDrinks(storeId)

        // Then
        verify { drinkRepository.findAll(storeId) }

        assertEquals(2, drinks.size)
        assertEquals("Coca-Cola", drinks[0].name)
        assertEquals("Pepsi", drinks[1].name)
    }

    @Test
    fun `should return a drink by id`() {
        // Given
        val drinkId = UUID.randomUUID()
        val storeId = UUID.randomUUID()

        val drink = Drink(
            id = drinkId,
            name = "Coca-Cola",
            milliliters = 0,
            alcoholPercentage = 0.0,
            drinkPackaging = DrinkPackaging.CAN,
            storeId = storeId,
        )

        every { drinkRepository.findById(drinkId, storeId) } returns drink

        // When
        val foundDrink = service.getDrinkById(drinkId, storeId)

        // Then
        verify { drinkRepository.findById(drinkId, storeId) }

        assertEquals("Coca-Cola", foundDrink?.name)
        assertEquals(drinkId, foundDrink?.id)
    }

    @Test
    fun `should not return a drink if id does not exist`() {
        // Given
        val drinkId = UUID.randomUUID()
        val storeId = UUID.randomUUID()

        every { drinkRepository.findById(drinkId, storeId) } returns null

        // When
        val foundDrink = service.getDrinkById(drinkId, storeId)

        // Then
        verify { drinkRepository.findById(drinkId, storeId) }

        assertEquals(null, foundDrink)
    }

    @Test
    fun `should create a new drink`() {
        // Given
        val createDrinkRequest = CreateDrinkRequest(
            name = "Coca-Cola",
            milliliters = 0,
            alcoholPercentage = 0.0,
            packaging = DrinkPackagingDTO.CAN,
        )

        val storeId = UUID.randomUUID()

        every { drinkRepository.create(any(), storeId) } answers {
            val drink = firstArg<Drink>()
            drink.copy(id = UUID.randomUUID())
        }

        // When
        val createdDrink = service.createDrink(createDrinkRequest, storeId)

        // Then
        verify { drinkRepository.create(any(), storeId) }

        assertEquals("Coca-Cola", createdDrink.name)
    }

    @Test
    fun `should delete a drink by id`() {
        // Given
        val drinkId = UUID.randomUUID()
        val storeId = UUID.randomUUID()

        every { drinkRepository.deleteById(any(), any()) } returns Unit

        // When
        service.deleteDrink(drinkId, storeId)

        // Then
        verify { drinkRepository.deleteById(drinkId, storeId) }
    }
}