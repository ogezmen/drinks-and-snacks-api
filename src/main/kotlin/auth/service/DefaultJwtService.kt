package de.okan.drink_and_snack_api.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.okan.drink_and_snack_api.configuration.model.JwtConfigurationProperties
import java.util.Date
import java.util.UUID

class DefaultJwtService(
    private val jwtConfigurationProperties: JwtConfigurationProperties
) : JwtService {
    override fun generateAccessToken(userId: String, roles: Set<String>): String {
        val algorithm = Algorithm.HMAC256(jwtConfigurationProperties.secret)
        return JWT.create()
            .withAudience(jwtConfigurationProperties.audience)
            .withIssuer(jwtConfigurationProperties.issuer)
            .withClaim("userId", userId)
            .withArrayClaim("roles", roles.toTypedArray())
            .withExpiresAt(Date(System.currentTimeMillis() + 600000)) // 10 minutes; TODO: Make this configurable
            .sign(algorithm)
    }

    override fun validateAccessToken(token: String): UUID? {
        val algorithm = Algorithm.HMAC256(jwtConfigurationProperties.secret)
        val verifier = JWT.require(algorithm)
            .withAudience(jwtConfigurationProperties.audience)
            .withIssuer(jwtConfigurationProperties.issuer)
            .build()
        val decodedJWT = verifier.verify(token)

        val userId = decodedJWT.getClaim("userId").asString()
        return UUID.fromString(userId)
    }
}