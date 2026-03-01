package auth.service

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import de.okan.drink_and_snack_api.auth.configuration.model.JwtConfiguration
import de.okan.drink_and_snack_api.auth.service.DefaultJwtService
import io.ktor.util.encodeBase64
import java.util.UUID
import kotlin.io.encoding.Base64
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultJwtServiceTest {

    lateinit var jwtService: DefaultJwtService

    @BeforeTest
    fun setup() {
        val jwtConfiguration = JwtConfiguration(
            secret = "secret",
            issuer = "issuer",
            audience = "audience",
        )
        jwtService = DefaultJwtService(jwtConfiguration)
    }

    @Test
    fun `generate access token and validate`() {
        val userId = UUID.randomUUID()
        val token = jwtService.generateAccessToken(userId)

        val validatedUserId = jwtService.validateAccessToken(token)

        assertEquals(userId, validatedUserId)
    }

    @Test(expected = JWTDecodeException::class)
    fun `should fail validation of non-base64 token`() {
        val invalidToken = "invalid.token.here"
        jwtService.validateAccessToken(invalidToken)
    }

    @Test(expected = SignatureVerificationException::class)
    fun `should fail validation of invalid JWT token`() {
        val jwtConfiguration2 = JwtConfiguration(
            secret = "secret2",
            issuer = "issuer2",
            audience = "audience2",
        )
        val jwtService2 = DefaultJwtService(jwtConfiguration2)

        val userId = UUID.randomUUID()
        val token2 = jwtService2.generateAccessToken(userId)

        jwtService.validateAccessToken(token2)
    }
}