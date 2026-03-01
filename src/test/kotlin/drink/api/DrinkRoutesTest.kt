package drink.api

import de.okan.drink_and_snack_api.auth.configuration.authConfiguration
import de.okan.drink_and_snack_api.auth.configuration.model.JwtConfiguration
import de.okan.drink_and_snack_api.configuration.UUIDSerializer
import de.okan.drink_and_snack_api.configureRouting
import de.okan.drink_and_snack_api.drink.api.model.CreateDrinkRequest
import de.okan.drink_and_snack_api.drink.api.model.DrinkDTO
import de.okan.drink_and_snack_api.drink.service.DrinkService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class DrinkRoutesTest {

    private val drinkService: DrinkService = mockk()
    private val storeId: UUID = UUID.randomUUID()

    fun setupTestApplication(block: suspend (HttpClient) -> Unit) = testApplication {

        application {
            val jwtConfiguration = JwtConfiguration(
                secret = "secret",
                issuer = "issuer",
                audience = "audience",
            )
            authConfiguration(jwtConfiguration)

            configureRouting(
                storeService = mockk(),
                drinkService = drinkService,
                authService = mockk(),
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        serializersModule = SerializersModule {
                            contextual(UUID::class, UUIDSerializer)
                        }
                    }
                )
            }
        }

        block(client)
    }

    @Test
    fun `should return all drinks for a store`() {
        val drinks = listOf(
            DrinkDTO(
                id = UUID.randomUUID(),
                name = "Coke",
            ),
            DrinkDTO(
                id = UUID.randomUUID(),
                name = "Pepsi",
            )
        )

        every { drinkService.getAllDrinks(storeId) } returns drinks

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
        )

        val createdDrink = DrinkDTO(
            id = UUID.randomUUID(),
            name = createDrinkRequest.name,
        )

        every { drinkService.createDrink(createDrinkRequest, storeId) } returns createdDrink

        setupTestApplication { client ->
            val response = client.post("/api/v1/stores/$storeId/drinks") {
                setBody(createDrinkRequest)
                contentType(ContentType.Application.Json)
            }
            val responseBody = response.body<DrinkDTO>()

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(createdDrink, responseBody)
        }
    }

    @Test
    fun `should delete a drink from a store`() {
        val drinkId = UUID.randomUUID()

        every { drinkService.deleteDrink(drinkId, storeId) } returns Unit

        setupTestApplication { client ->
            val response = client.delete("/api/v1/stores/$storeId/drinks/$drinkId")

            verify { drinkService.deleteDrink(drinkId, storeId) }

            assertEquals(HttpStatusCode.NoContent, response.status)
        }
    }
}