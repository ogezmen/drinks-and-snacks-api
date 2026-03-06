package drink.api

import configuration.authTestConfiguration
import configuration.runTestApplication
import de.okan.drinks_and_snacks_api.configureRouting
import de.okan.drinks_and_snacks_api.drink.api.model.CreateDrinkRequest
import de.okan.drinks_and_snacks_api.drink.api.model.DrinkDTO
import de.okan.drinks_and_snacks_api.drink.api.model.DrinkPackagingDTO
import de.okan.drinks_and_snacks_api.drink.api.setupDrinkRoutes
import de.okan.drinks_and_snacks_api.drink.service.DrinkService
import de.okan.drinks_and_snacks_api.store.service.StoreService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class DrinkRoutesTest {

    val drinkService: DrinkService = mockk()
    val storeService: StoreService = mockk()
    val storeId: UUID = UUID.randomUUID()
    val userId: UUID = UUID.randomUUID()

    fun setupTestApplication(block: suspend (HttpClient) -> Unit) = testApplication {

        application {
            authTestConfiguration(userId)

            configureRouting()

            routing {
                setupDrinkRoutes(
                    drinkService = drinkService,
                    storeService = storeService,
                )
            }
        }
        runTestApplication(block)
    }

    @Test
    fun `should return all drinks for a store`() {
        val drinks = listOf(
            DrinkDTO(
                id = UUID.randomUUID(),
                name = "Coke",
                milliliters = 0,
                alcoholPercentage = 0.0,
                packaging = DrinkPackagingDTO.CAN
            ),
            DrinkDTO(
                id = UUID.randomUUID(),
                name = "Pepsi",
                milliliters = 0,
                alcoholPercentage = 0.0,
                packaging = DrinkPackagingDTO.CAN
            )
        )

        every { drinkService.getAllDrinks(any(), any()) } returns drinks

        setupTestApplication { client ->
            val response = client.get("/api/v1/stores/$storeId/drinks")
            val responseBody = response.body<List<DrinkDTO>>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(drinks, responseBody)
        }
    }

    @Test
    fun `should return drink by id for a store`() {
        val drinkId = UUID.randomUUID()
        val drink = DrinkDTO(
            id = drinkId,
            name = "Coke",
            milliliters = 0,
            alcoholPercentage = 0.0,
            packaging = DrinkPackagingDTO.CAN
        )

        every { drinkService.getDrinkById(drinkId, storeId) } returns drink

        setupTestApplication { client ->
            val response = client.get("/api/v1/stores/$storeId/drinks/$drinkId")
            val responseBody = response.body<DrinkDTO>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(drink, responseBody)
        }
    }

    @Test
    fun `should return not found when drink does not exist for a store`() {
        val drinkId = UUID.randomUUID()

        every { drinkService.getDrinkById(drinkId, storeId) } returns null

        setupTestApplication { client ->
            val response = client.get("/api/v1/stores/$storeId/drinks/$drinkId")

            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    @Test
    fun `should add a new drink to a store`() {
        val createDrinkRequest = CreateDrinkRequest(
            name = "Coke",
            milliliters = 0,
            alcoholPercentage = 0.0,
            packaging = DrinkPackagingDTO.CAN
        )

        val createdDrink = DrinkDTO(
            id = UUID.randomUUID(),
            name = createDrinkRequest.name,
            milliliters = createDrinkRequest.milliliters,
            alcoholPercentage = createDrinkRequest.alcoholPercentage,
            packaging = createDrinkRequest.packaging,
        )

        every { drinkService.createDrink(any(), any()) } returns createdDrink
        every { storeService.isOwnerOfStore(any(), any()) } returns true

        setupTestApplication { client ->
            val response = client.post("/api/v1/stores/$storeId/drinks") {
                setBody(createDrinkRequest)
                contentType(ContentType.Application.Json)
            }
            val responseBody = response.body<DrinkDTO>()

            verify { drinkService.createDrink(createDrinkRequest, storeId) }
            verify { storeService.isOwnerOfStore(userId, storeId) }

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(createdDrink, responseBody)
        }
    }

    @Test
    fun `should add not a new drink to a store if user is not owner of store`() {
        val createDrinkRequest = CreateDrinkRequest(
            name = "Coke",
            milliliters = 0,
            alcoholPercentage = 0.0,
            packaging = DrinkPackagingDTO.CAN
        )

        every { storeService.isOwnerOfStore(any(), any()) } returns false

        setupTestApplication { client ->
            val response = client.post("/api/v1/stores/$storeId/drinks") {
                setBody(createDrinkRequest)
                contentType(ContentType.Application.Json)
            }

            verify { storeService.isOwnerOfStore(userId, storeId) }

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    @Test
    fun `should delete a drink from a store`() {
        val drinkId = UUID.randomUUID()

        every { drinkService.deleteDrink(any(), any()) } returns Unit
        every { storeService.isOwnerOfStore(any(), any()) } returns true

        setupTestApplication { client ->
            val response = client.delete("/api/v1/stores/$storeId/drinks/$drinkId")

            verify { drinkService.deleteDrink(drinkId, storeId) }
            verify { storeService.isOwnerOfStore(userId, storeId) }

            assertEquals(HttpStatusCode.NoContent, response.status)
        }
    }

    @Test
    fun `should not delete if user is not owner of store`() {
        val drinkId = UUID.randomUUID()

        every { storeService.isOwnerOfStore(any(), any()) } returns false

        setupTestApplication { client ->
            val response = client.delete("/api/v1/stores/$storeId/drinks/$drinkId")

            verify { storeService.isOwnerOfStore(userId, storeId) }

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }
}