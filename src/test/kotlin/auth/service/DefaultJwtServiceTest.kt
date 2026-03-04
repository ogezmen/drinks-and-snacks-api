package auth.service

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import de.okan.drink_and_snack_api.configuration.model.JwtConfigurationProperties
import de.okan.drink_and_snack_api.auth.service.DefaultJwtService
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultJwtServiceTest {

    lateinit var jwtService: DefaultJwtService

    @BeforeTest
    fun setup() {
        val jwtConfigurationProperties = JwtConfigurationProperties(
            secret = "secret",
            issuer = "issuer",
            audience = "audience",
        )
        jwtService = DefaultJwtService(jwtConfigurationProperties)
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
        val jwtConfigurationProperties2 = JwtConfigurationProperties(
            secret = "secret2",
            issuer = "issuer2",
            audience = "audience2",
        )
        val jwtService2 = DefaultJwtService(jwtConfigurationProperties2)

        val userId = UUID.randomUUID()
        val token2 = jwtService2.generateAccessToken(userId)

        jwtService.validateAccessToken(token2)
    }
}