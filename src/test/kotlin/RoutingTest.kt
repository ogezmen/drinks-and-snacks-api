import de.okan.drink_and_snack_api.auth.configuration.authConfiguration
import de.okan.drink_and_snack_api.auth.configuration.model.JwtConfiguration
import de.okan.drink_and_snack_api.configureRouting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class RoutingTest {

    @Test
    fun `should return alive message on root endpoint`() = testApplication {
        application {
            val jwtConfiguration = JwtConfiguration(
                secret = "secret",
                issuer = "issuer",
                audience = "audience",
            )
            authConfiguration(jwtConfiguration)

            configureRouting(
                storeService = mockk(),
                drinkService = mockk(),
                authService = mockk(),
            )
        }

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
    }
}