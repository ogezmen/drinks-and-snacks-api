package de.okan.drinks_and_snacks_api.drink.api

import de.okan.drinks_and_snacks_api.drink.api.model.CreateDrinkRequest
import de.okan.drinks_and_snacks_api.drink.service.DrinkService
import de.okan.drinks_and_snacks_api.requireUUID
import de.okan.drinks_and_snacks_api.requireUserIDFromJWT
import de.okan.drinks_and_snacks_api.store.service.StoreService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.setupDrinkRoutes(
    drinkService: DrinkService,
    storeService: StoreService,
) {
    route("/api/v1/stores/{storeId}/drinks") {
        get {
            val storeId = call.requireUUID("storeId")
            val filters = call.request.queryParameters.toDrinkFiltersDTO()
            val drinks = drinkService.getAllDrinks(storeId, filters)
            call.respond(drinks)
        }

        get("/{id}") {
            val id = call.requireUUID()
            val storeId = call.requireUUID("storeId")
            val drink = drinkService.getDrinkById(id, storeId) ?: throw NoSuchElementException("Drink not found")
            call.respond(drink)

        }

        authenticate("auth-jwt") {
            post {
                val storeId = call.requireUUID("storeId")
                val storeOwnerUserId = call.requireUserIDFromJWT()

                require(storeService.isOwnerOfStore(storeOwnerUserId, storeId)) {
                    "Store with id $storeId not owned by owner with id $storeOwnerUserId"
                }

                val drink = call.receive<CreateDrinkRequest>()
                val createdDrink = drinkService.createDrink(drink, storeId)
                call.respond(message = createdDrink, status = HttpStatusCode.Created)
            }

            delete("/{id}") {
                val storeId = call.requireUUID("storeId")
                val storeOwnerUserId = call.requireUserIDFromJWT()

                require(storeService.isOwnerOfStore(storeOwnerUserId, storeId)) {
                    "Store with id $storeId not owned by owner with id $storeOwnerUserId"
                }

                val id = call.requireUUID()
                drinkService.deleteDrink(id, storeId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}