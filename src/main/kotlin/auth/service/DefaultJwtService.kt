package de.okan.drink_and_snack_api.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.okan.drink_and_snack_api.auth.configuration.model.JwtConfiguration
import java.util.Date
import java.util.UUID

class DefaultJwtService(
    private val jwtConfiguration: JwtConfiguration
) : JwtService {
    override fun generateAccessToken(userId: UUID): String {
        val algorithm = Algorithm.HMAC256(jwtConfiguration.secret)
        return JWT.create()
            .withAudience(jwtConfiguration.audience)
            .withIssuer(jwtConfiguration.issuer)
            .withClaim("userId", userId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + 600000)) // 10 minutes; TODO: Make this configurable
            .sign(algorithm)
    }

    override fun validateAccessToken(token: String): String? {
        val algorithm = Algorithm.HMAC256(jwtConfiguration.secret)
        val verifier = JWT.require(algorithm)
            .withAudience(jwtConfiguration.audience)
            .withIssuer(jwtConfiguration.issuer)
            .build()
        val decodedJWT = verifier.verify(token)
        return decodedJWT.getClaim("userId").asString()
    }
}