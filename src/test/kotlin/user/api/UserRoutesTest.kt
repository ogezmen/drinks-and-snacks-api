package user.api

import configuration.authTestConfiguration
import configuration.runTestApplication
import de.okan.drink_and_snack_api.configureRouting
import de.okan.drink_and_snack_api.user.api.model.DeleteAccountRequest
import de.okan.drink_and_snack_api.user.api.model.UserDTO
import de.okan.drink_and_snack_api.user.api.setupUserRoutes
import de.okan.drink_and_snack_api.user.service.UserService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRoutesTest {

    val userService: UserService = mockk()
    val userId = UUID.randomUUID()

    fun setupTestApplication(block: suspend (HttpClient) -> Unit) = testApplication {

        application {
            authTestConfiguration(userId)

            configureRouting()

            routing {
                setupUserRoutes(userService)
            }
        }
        runTestApplication(block)
    }

    @Test
    fun `should return user data`() {

        val userDTO = UserDTO(
            username = "username",
            firstName = "firstName",
            lastName = "lastName",
        )

        every { userService.getUserById(userId) } returns userDTO

        setupTestApplication { client ->
            val response = client.get("/api/v1/users/me")
            val responseBody = response.body<UserDTO>()

            assertEquals(userDTO, responseBody)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun `should not return user data if user is not found`() {

        every { userService.getUserById(userId) } returns null

        setupTestApplication { client ->
            val response = client.get("/api/v1/users/me")

            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }


    @Test
    fun `should delete account`() {

        val deleteAccountRequest = DeleteAccountRequest(
            password = "password",
        )

        every { userService.deleteUserById(userId, any()) } returns Unit

        setupTestApplication { client ->
            val response = client.delete("/api/v1/users/me") {
                setBody(deleteAccountRequest)
                contentType(ContentType.Application.Json)
            }

            assertEquals(HttpStatusCode.NoContent, response.status)
        }
    }

}