package de.okan.drinks_and_snacks_api

import com.auth0.jwt.interfaces.Claim
import de.okan.drinks_and_snacks_api.auth.api.setupAuthRoutes
import de.okan.drinks_and_snacks_api.auth.service.AuthService
import de.okan.drinks_and_snacks_api.configuration.UUIDSerializer
import de.okan.drinks_and_snacks_api.drink.api.setupDrinkRoutes
import de.okan.drinks_and_snacks_api.drink.service.DrinkService
import de.okan.drinks_and_snacks_api.store.api.setupStoreRoutes
import de.okan.drinks_and_snacks_api.store.service.StoreService
import de.okan.drinks_and_snacks_api.user.api.setupUserRoutes
import de.okan.drinks_and_snacks_api.user.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.koin.java.KoinJavaComponent.getKoin
import java.util.UUID


fun Application.configureRouting() {
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
        exception<BadRequestException> { call, _ ->
            call.respondText("Bad request", status = HttpStatusCode.BadRequest)
        }
        exception<IllegalArgumentException> { call, cause ->
            call.respondText(cause.message ?: "Bad request", status = HttpStatusCode.BadRequest)
        }
        exception<NoSuchElementException> { call, cause ->
            call.respondText(cause.message ?: "Resource not found", status = HttpStatusCode.NotFound)
        }
        exception<Throwable> { call, _ ->
            call.respondText("Unknown error", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.setupSwaggerRoute() {
    swaggerUI(
        path = "swagger-ui"
    )
}

fun Route.setupRootRoutes() {
    get("/") {
        call.respondText("I'm alive!")
    }
}


fun Route.setupRoutes(
    storeService: StoreService = getKoin().get(),
    drinkService: DrinkService = getKoin().get(),
    userService: UserService = getKoin().get(),
    authService: AuthService = getKoin().get(),
) {
    setupRootRoutes()
    setupSwaggerRoute()
    setupAuthRoutes(authService)
    setupUserRoutes(userService)
    setupStoreRoutes(storeService)
    setupDrinkRoutes(drinkService, storeService)
}

fun ApplicationCall.requireUUID(parameterName: String = "id"): UUID {
    return parameters[parameterName]
        ?.let { UUID.fromString(it)}
        ?: throw IllegalArgumentException("Missing or malformed $parameterName")
}

fun ApplicationCall.requireClaimFromJWT(claimName: String): Claim {
    val principal = principal<JWTPrincipal>()
    return principal
        ?.payload
        ?.getClaim(claimName)
        ?: throw IllegalArgumentException("Missing $claimName in claims")
}

fun ApplicationCall.requireUserIDFromJWT(): UUID {

    return requireClaimFromJWT("userId")
        .asString()
        .let { UUID.fromString(it) }
}

fun ApplicationCall.requireRoleFromJWT(): List<String> {

    return requireClaimFromJWT("roles")
        .asList(String::class.java)
}