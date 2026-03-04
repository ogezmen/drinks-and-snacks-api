package de.okan.drink_and_snack_api.auth.configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.okan.drink_and_snack_api.auth.configuration.model.JwtConfiguration
import de.okan.drink_and_snack_api.user.persistence.ExposedUserRepository
import de.okan.drink_and_snack_api.user.persistence.UserRepository
import de.okan.drink_and_snack_api.auth.service.AuthService
import de.okan.drink_and_snack_api.auth.service.BCryptPasswordService
import de.okan.drink_and_snack_api.auth.service.DefaultAuthService
import de.okan.drink_and_snack_api.auth.service.DefaultJwtService
import de.okan.drink_and_snack_api.auth.service.JwtService
import de.okan.drink_and_snack_api.auth.service.PasswordService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.response.respondText
import org.koin.dsl.module

fun jwtConfigurationModule(applicationConfig: ApplicationConfig) = module {
    single<JwtConfiguration> {
        JwtConfiguration(
            secret = applicationConfig.property("auth.jwt.secret").getString(),
            issuer = applicationConfig.property("auth.jwt.issuer").getString(),
            audience = applicationConfig.property("auth.jwt.audience").getString(),
        )
    }
}

val authModule = module {
    single<UserRepository> { ExposedUserRepository(database = get()) }
    single<PasswordService> { BCryptPasswordService() }
    single<JwtService> { DefaultJwtService(jwtConfiguration = get()) }
    single<AuthService> { DefaultAuthService(
        userRepository = get(),
        passwordService = get(),
        jwtService = get(),
    )}
}

fun Application.authConfiguration(jwtConfiguration: JwtConfiguration) {

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Drinks and Snacks API"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtConfiguration.secret))
                    .withAudience(jwtConfiguration.audience)
                    .withIssuer(jwtConfiguration.issuer)
                    .build()
            )
            validate { credential ->
                if (!credential.payload.audience.contains(jwtConfiguration.audience)) {
                    return@validate null
                }
                JWTPrincipal(credential.payload)
            }
            challenge { _, _ -> call.respondText("Token is not valid or has expired", status = HttpStatusCode.Unauthorized) }
        }
    }
}