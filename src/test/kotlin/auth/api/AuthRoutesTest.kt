package auth.api

import de.okan.drink_and_snack_api.auth.api.model.LoginRequest
import de.okan.drink_and_snack_api.auth.api.model.RegisterRequest
import de.okan.drink_and_snack_api.auth.api.model.SessionDTO
import de.okan.drink_and_snack_api.auth.service.AuthService
import de.okan.drink_and_snack_api.configuration.UUIDSerializer
import de.okan.drink_and_snack_api.configureRouting
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRoutesTest {

    val authService = mockk<AuthService>()

    fun setupTestApplication(block: suspend (HttpClient) -> Unit) = testApplication {

        application {
            configureRouting(
                storeService = mockk(),
                drinkService = mockk(),
                authService = authService,
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
    fun `test register`() {

        val registerRequest = RegisterRequest(
            username = "testUser",
            password = "testPassword",
            firstName = "testFirstName",
            lastName = "testLastName",
        )

        val sessionDTO = SessionDTO(
            accessToken = "accessToken",
        )

        setupTestApplication { client ->
            every { authService.register(registerRequest) } returns sessionDTO

            val response = client.post("/api/v1/auth/register") {
                setBody(registerRequest)
                contentType(ContentType.Application.Json)
            }

            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    @Test
    fun `test login`() {
        val loginRequest = LoginRequest(
            username = "testUser",
            password = "testPassword",
        )

        val sessionDTO = SessionDTO(
            accessToken = "accessToken",
        )

        setupTestApplication { client ->

            every { authService.login(loginRequest) } returns sessionDTO

            val response = client.post("/api/v1/auth/login") {
                setBody(loginRequest)
                contentType(ContentType.Application.Json)
            }

            assertEquals(HttpStatusCode.OK, response.status)
        }
    }
}