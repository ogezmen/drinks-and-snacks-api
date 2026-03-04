package store.api

import configuration.authTestConfiguration
import configuration.runTestApplication
import de.okan.drink_and_snack_api.configureRouting
import de.okan.drink_and_snack_api.store.api.model.CreateStoreRequest
import de.okan.drink_and_snack_api.store.api.setupStoreRoutes
import de.okan.drink_and_snack_api.store.service.StoreService
import de.okan.drink_and_snack_api.store.service.model.StoreDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StoreRoutesTest {
    private val storeService: StoreService = mockk()
    val userId: UUID = UUID.randomUUID()

    private fun setupTestApplication(block: suspend (HttpClient) -> Unit) = testApplication {
        application {
            authTestConfiguration(userId)

            configureRouting()

            routing {
                setupStoreRoutes(storeService)
            }
        }

        runTestApplication(block)
    }

    @Test
    fun `should return all stores`() {

        val stores = listOf(
            StoreDTO(
                id = UUID.randomUUID(),
                name = "Store 1",
            ),
            StoreDTO(
                id = UUID.randomUUID(),
                name = "Store 2",
            )
        )

        every { storeService.getAllStores() } returns stores

        setupTestApplication { client ->
            val response = client.get("/api/v1/stores")

            val responseBody = response.body<List<StoreDTO>>()
            assertTrue(response.status.isSuccess())
            assertEquals(stores.size, responseBody.size)
            assertEquals(responseBody[0], stores[0])
            assertEquals(responseBody[1], stores[1])
        }
    }

    @Test
    fun `should return store by id`() {
        val storeId = UUID.randomUUID()
        val store = StoreDTO(
            id = storeId,
            name = "Store 1",
        )

        every { storeService.getStoreById(any()) } returns store

        setupTestApplication { client ->
            val response = client.get("/api/v1/stores/$storeId")

            val responseBody = response.body<StoreDTO>()

            verify { storeService.getStoreById(storeId) }
            assertTrue(response.status.isSuccess())
            assertEquals(store, responseBody)
        }
    }

    @Test
    fun `should return 404 if store not found`() {
        val storeId = UUID.randomUUID()

        every { storeService.getStoreById(any()) } returns null

        setupTestApplication { client ->
            val response = client.get("/api/v1/stores/$storeId")

            verify { storeService.getStoreById(storeId) }
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    @Test
    fun `should return 400 if id is invalid`() {
        setupTestApplication { client ->
            val response = client.get("/api/v1/stores/invalid-id")

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    @Test
    fun `should create a new store`() {
        val createStoreRequest = CreateStoreRequest(
            name = "New Store"
        )

        val createdStore = StoreDTO(
            id = UUID.randomUUID(),
            name = createStoreRequest.name,
        )

        every { storeService.createStore(any(), any()) } returns createdStore

        setupTestApplication { client ->
            val response = client.post("/api/v1/stores") {
                contentType(ContentType.Application.Json)
                setBody(createStoreRequest)
            }
            verify { storeService.createStore(createStoreRequest, userId) }

            val responseBody = response.body<StoreDTO>()
            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(createdStore, responseBody)
        }
    }

    @Test
    fun `should delete a store`() {
        val storeId = UUID.randomUUID()

        every { storeService.deleteStore(any(), userId) } returns Unit

        setupTestApplication { client ->
            val response = client.delete("/api/v1/stores/$storeId")

            verify { storeService.deleteStore(storeId, userId) }

            assertEquals(HttpStatusCode.NoContent, response.status)
        }
    }
}