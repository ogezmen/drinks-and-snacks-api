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
import io.mockk.verify
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRoutesTest {

    val userService: UserService = mockk()
    val userId = UUID.randomUUID()

    fun setupTestApplication(roles: List<String> = listOf(), block: suspend (HttpClient) -> Unit) = testApplication {

        application {
            authTestConfiguration(userId, roles)

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
            roles = setOf()
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

    @Test
    fun `should return all users for admin`() {

        every { userService.getUsers() } returns mockk(relaxed = true)

        setupTestApplication(listOf("ADMIN")) { client ->
            val response = client.get("/api/v1/users")

            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun `should not return all users for non-admin`() {

        every { userService.getUsers() } returns mockk(relaxed = true)

        setupTestApplication { client ->
            val response = client.get("/api/v1/users")

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
    }

    @Test
    fun `should return user by id for admin`() {

        val id = UUID.randomUUID()

        val userDTO = UserDTO(
            username = "username",
            firstName = "firstName",
            lastName = "lastName",
            roles = setOf()
        )

        every { userService.getUserById(any()) } returns userDTO

        setupTestApplication(listOf("ADMIN")) { client ->
            val response = client.get("/api/v1/users/$id")
            val responseBody = response.body<UserDTO>()

            assertEquals(HttpStatusCode.OK, response.status)

            assertEquals(userDTO.username, responseBody.username)
            assertEquals(userDTO.firstName, responseBody.firstName)
            assertEquals(userDTO.lastName, responseBody.lastName)
        }
    }

    @Test
    fun `should return user by id for admin if user is not found`() {

        val id = UUID.randomUUID()

        every { userService.getUserById(any()) } returns null

        setupTestApplication(listOf("ADMIN")) { client ->
            val response = client.get("/api/v1/users/$id")

            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    @Test
    fun `should not return user by id for non-admin`() {

        val id = UUID.randomUUID()

        every { userService.getUserById(any()) } returns mockk(relaxed = true)

        setupTestApplication { client ->
            val response = client.get("/api/v1/users/$id")

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
    }

    @Test
    fun `should delete user by id for admin`() {

        val id = UUID.randomUUID()

        every { userService.deleteUserById(any()) } returns Unit

        setupTestApplication(listOf("ADMIN")) { client ->
            val response = client.delete("/api/v1/users/$id")

            assertEquals(HttpStatusCode.NoContent, response.status)

            verify { userService.deleteUserById(id) }
        }
    }

    @Test
    fun `should not delete user by id for non-admin`() {

        val id = UUID.randomUUID()

        every { userService.deleteUserById(any()) } returns Unit

        setupTestApplication{ client ->
            val response = client.delete("/api/v1/users/$id")

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
    }
}