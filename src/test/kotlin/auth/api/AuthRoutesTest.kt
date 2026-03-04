package auth.api

import configuration.runTestApplication
import de.okan.drink_and_snack_api.auth.api.model.LoginRequest
import de.okan.drink_and_snack_api.auth.api.model.RegisterRequest
import de.okan.drink_and_snack_api.auth.api.model.SessionDTO
import de.okan.drink_and_snack_api.auth.api.setupAuthRoutes
import de.okan.drink_and_snack_api.configuration.authConfiguration
import de.okan.drink_and_snack_api.configuration.model.JwtConfigurationProperties
import de.okan.drink_and_snack_api.auth.service.AuthService
import de.okan.drink_and_snack_api.configureRouting
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRoutesTest {

    val authService = mockk<AuthService>()

    fun setupTestApplication(block: suspend (HttpClient) -> Unit) = testApplication {

        application {
            val jwtConfigurationProperties = JwtConfigurationProperties(
                secret = "secret",
                issuer = "issuer",
                audience = "audience",
            )
            authConfiguration(jwtConfigurationProperties)

            configureRouting()

            routing {
                setupAuthRoutes(
                    authService = authService,
                )
            }
        }

        runTestApplication(block)
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