package de.okan.drinks_and_snacks_api.store.api

import de.okan.drinks_and_snacks_api.requireUUID
import de.okan.drinks_and_snacks_api.requireUserIDFromJWT
import de.okan.drinks_and_snacks_api.store.api.dto.CreateStoreRequest
import de.okan.drinks_and_snacks_api.store.service.StoreService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.setupStoreRoutes(
    storeService: StoreService,
) {
    route("/api/v1/stores") {
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
    }
}