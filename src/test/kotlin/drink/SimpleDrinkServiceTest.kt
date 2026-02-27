package drink

import com.example.drink.repository.DrinkRepository
import com.example.drink.domain.Drink
import com.example.drink.service.SimpleDrinkService
import com.example.drink.api.model.DrinkDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleDrinkServiceTest {

    private val drinkRepository = mockk<DrinkRepository>()
    private val service = SimpleDrinkService(drinkRepository)

    @Test
    fun `should return all drinks`() {
        val storeId = UUID.randomUUID()

        val drink1 = Drink(UUID.randomUUID(), "Coca-Cola", storeId)
        val drink2 = Drink(UUID.randomUUID(), "Pepsi", storeId)

        every { drinkRepository.findAll(any()) } returns listOf(drink1, drink2)


        val drinks = service.getAllDrinks(storeId)

        verify { drinkRepository.findAll(storeId) }

        assertEquals(2, drinks.size)
        assertEquals("Coca-Cola", drinks[0].name)
        assertEquals("Pepsi", drinks[1].name)
    }

    @Test
    fun `should return a drink by id`() {
        val drinkId = UUID.randomUUID()
        val storeId = UUID.randomUUID()

        val drink = Drink(drinkId, "Coca-Cola", storeId)


        every { drinkRepository.findById(drinkId, storeId) } returns drink

        val foundDrink = service.getDrinkById(drinkId, storeId)

        verify { drinkRepository.findById(drinkId, storeId) }

        assertEquals("Coca-Cola", foundDrink?.name)
        assertEquals(drinkId, foundDrink?.id)
    }

    @Test
    fun `should not return a drink if id does not exist`() {
        val drinkId = UUID.randomUUID()
        val storeId = UUID.randomUUID()

        every { drinkRepository.findById(drinkId, storeId) } returns null

        val foundDrink = service.getDrinkById(drinkId, storeId)

        verify { drinkRepository.findById(drinkId, storeId) }

        assertEquals(null, foundDrink)
    }

    @Test
    fun `should create a new drink`() {

        val drinkDTO = DrinkDTO(
            name = "Coca-Cola"
        )

        val storeId = UUID.randomUUID()

        every { drinkRepository.save(any(), storeId) } answers {
            val drink = firstArg<Drink>()
            drink.copy(id = UUID.randomUUID())
        }

        val createdDrink = service.addDrink(drinkDTO, storeId)

        verify { drinkRepository.save(any(), storeId) }

        assertEquals("Coca-Cola", createdDrink.name)
    }

    @Test
    fun `should delete a drink by id`() {
        val drinkId = UUID.randomUUID()
        val storeId = UUID.randomUUID()

        every { drinkRepository.deleteById(drinkId, storeId) } returns Unit

        service.deleteDrink(drinkId, storeId)

        verify { drinkRepository.deleteById(drinkId, storeId) }
    }
}