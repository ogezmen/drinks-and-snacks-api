package drink

import com.example.drink.repository.DrinkRepository
import com.example.drink.repository.entity.Drink
import com.example.drink.service.SimpleDrinkService
import com.example.drink.service.model.DrinkDTO
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
        val drink1 = Drink(UUID.randomUUID(), "Coca-Cola")
        val drink2 = Drink(UUID.randomUUID(), "Pepsi")

        every { drinkRepository.findAll() } returns listOf(drink1, drink2)

        val drinks = service.getAllDrinks()

        verify { drinkRepository.findAll() }

        assertEquals(2, drinks.size)
        assertEquals("Coca-Cola", drinks[0].name)
        assertEquals("Pepsi", drinks[1].name)
    }

    @Test
    fun `should return a drink by id`() {
        val drinkId = UUID.randomUUID()
        val drink = Drink(drinkId, "Coca-Cola")

        every { drinkRepository.findById(drinkId) } returns drink

        val foundDrink = service.getDrinkById(drinkId)

        verify { drinkRepository.findById(drinkId) }

        assertEquals("Coca-Cola", foundDrink?.name)
    }

    @Test
    fun `should create a new drink`() {

        val drinkDTO = DrinkDTO(
            name = "Coca-Cola"
        )

        every { drinkRepository.save(any()) } answers {
            val drink = firstArg<Drink>()
            drink.copy(id = UUID.randomUUID())
        }

        val createdDrink = service.addDrink(drinkDTO)

        verify { drinkRepository.save(any()) }

        assertEquals("Coca-Cola", createdDrink.name)
    }

    @Test
    fun `should delete a drink by id`() {
        val drinkId = UUID.randomUUID()

        every { drinkRepository.deleteById(drinkId) } returns Unit

        service.deleteDrink(drinkId)

        verify { drinkRepository.deleteById(drinkId) }
    }
}