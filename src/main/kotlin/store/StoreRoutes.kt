package com.example.store

import com.example.drink.drinkRoutes
import com.example.drink.service.DrinkService
import com.example.getIdAsUUID
import com.example.store.model.CreateStoreRequest
import com.example.store.service.StoreService
import io.ktor.http.HttpStatusCode
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

        post {
            val createStoreRequest = call.receive<CreateStoreRequest>()
            val createdStore = storeService.createStore(createStoreRequest)
            call.respond(message = createdStore, status = HttpStatusCode.Created)
        }

        route("/{id}") {
            get {
                val id = call.getIdAsUUID()
                if (id == null) {
                    call.respond(message = "Missing or malformed id", status = HttpStatusCode.BadRequest)
                    return@get
                }

                val store = storeService.getStoreById(id)
                if (store == null) {
                    call.respond(message = "Store not found", status = HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(store)
            }

            delete {
                val id = call.getIdAsUUID()
                if (id == null) {
                    call.respond(message = "Missing or malformed id", status = HttpStatusCode.BadRequest)
                    return@delete
                }

                storeService.deleteStore(id)
            }

        }

        drinkRoutes(drinkService)
    }
}