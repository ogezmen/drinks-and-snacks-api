package de.okan.drink_and_snack_api

import de.okan.drink_and_snack_api.auth.api.authRoutes
import de.okan.drink_and_snack_api.auth.service.AuthService
import de.okan.drink_and_snack_api.configuration.UUIDSerializer
import de.okan.drink_and_snack_api.drink.service.DrinkService
import de.okan.drink_and_snack_api.store.api.storeRoutes
import de.okan.drink_and_snack_api.store.service.StoreService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.util.UUID


fun Application.configureRouting(
    storeService: StoreService,
    drinkService: DrinkService,
    authService: AuthService,
) {
    install(ContentNegotiation) {
        json(
            Json {
                serializersModule = SerializersModule {
                    contextual(UUID::class, UUIDSerializer)
                }
            }
        )
    }

    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respondText(cause.message ?: "Bad request", status = HttpStatusCode.BadRequest)
        }
    }

    routing {
        swaggerUI(
            path = "swagger-ui"
        )

        get("/") {
            call.respondText("I'm alive!")
        }


        route("/api/v1") {
            storeRoutes(
                storeService = storeService,
                drinkService = drinkService,
            )
            authRoutes(authService)
        }
    }
}

private fun ApplicationCall.getParameterAsUUID(parameterName: String): UUID? {
    val id = parameters[parameterName]
    return try {
        UUID.fromString(id)
    } catch (_: IllegalArgumentException) {
        null
    }
}

fun ApplicationCall.requireUUID(parameterName: String = "id"): UUID {
    val uuid = getParameterAsUUID(parameterName)
    return uuid ?: throw IllegalArgumentException("Missing or malformed $parameterName")
}

fun ApplicationCall.requireUserIDFromJWT(): UUID {

    val principal = principal<JWTPrincipal>()
    return principal
        ?.payload
        ?.getClaim("userId")
        ?.asString()
        ?.let { UUID.fromString(it) }
        ?: throw IllegalArgumentException("Missing id in claims")
}