package de.okan.drinks_and_snacks_api.configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.okan.drinks_and_snacks_api.configuration.model.JwtConfigurationProperties
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*


fun Application.authConfiguration(jwtConfigurationProperties: JwtConfigurationProperties) {

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Drinks and Snacks API"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtConfigurationProperties.secret))
                    .withAudience(jwtConfigurationProperties.audience)
                    .withIssuer(jwtConfigurationProperties.issuer)
                    .build()
            )
            validate { credential ->
                if (!credential.payload.audience.contains(jwtConfigurationProperties.audience)) {
                    return@validate null
                }
                JWTPrincipal(credential.payload)
            }
            challenge { _, _ -> call.respondText("Token is not valid or has expired", status = HttpStatusCode.Unauthorized) }
        }
    }
}