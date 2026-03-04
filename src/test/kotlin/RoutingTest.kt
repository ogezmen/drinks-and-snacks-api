import de.okan.drink_and_snack_api.configuration.authConfiguration
import de.okan.drink_and_snack_api.configuration.model.JwtConfigurationProperties
import de.okan.drink_and_snack_api.configureRouting
import de.okan.drink_and_snack_api.setupRootRoutes
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.routing
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RoutingTest {

    @Test
    fun `should return alive message on root endpoint`() = testApplication {
        application {
            val jwtConfigurationProperties = JwtConfigurationProperties(
                secret = "secret",
                issuer = "issuer",
                audience = "audience",
            )
            authConfiguration(jwtConfigurationProperties)

            configureRouting()

            routing {
                setupRootRoutes()
            }
        }

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
    }
}