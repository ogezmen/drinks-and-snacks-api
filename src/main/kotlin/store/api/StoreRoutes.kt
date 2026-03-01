package de.okan.drink_and_snack_api.store.api

import de.okan.drink_and_snack_api.drink.api.drinkRoutes
import de.okan.drink_and_snack_api.drink.service.DrinkService
import de.okan.drink_and_snack_api.requireUUID
import de.okan.drink_and_snack_api.requireUserIDFromJWT
import de.okan.drink_and_snack_api.store.api.model.CreateStoreRequest
import de.okan.drink_and_snack_api.store.service.StoreService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.storeRoutes(storeService: StoreService, drinkService: DrinkService) {
    route("/stores") {
        get {
            call.respond(storeService.getAllStores())
        }

        get("/{id}") {
            val id = call.requireUUID()

            val store = storeService.getStoreById(id) ?: throw NoSuchElementException("Store not found")

            call.respond(store)
        }

        authenticate("auth-jwt") {
            post {
                val userId = call.requireUserIDFromJWT()

                val createStoreRequest = call.receive<CreateStoreRequest>()
                val createdStore = storeService.createStore(createStoreRequest, userId)

                call.respond(message = createdStore, status = HttpStatusCode.Created)
            }

            delete("/{id}") {
                val id = call.requireUUID()
                val ownerUserId = call.requireUserIDFromJWT()

                storeService.deleteStore(id, ownerUserId)

                call.respond(HttpStatusCode.NoContent)
            }
        }

        drinkRoutes(drinkService, storeService)
    }
}