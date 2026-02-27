package de.okan.drink_and_snack_api.drink.api

import de.okan.drink_and_snack_api.drink.api.model.CreateDrinkRequest
import de.okan.drink_and_snack_api.drink.service.DrinkService
import de.okan.drink_and_snack_api.requireUUID
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.drinkRoutes(drinkService: DrinkService) {
    route("/{storeId}/drinks") {
        get {
            val storeId = call.requireUUID("storeId")
            call.respond(drinkService.getAllDrinks(storeId))
        }

        get("/{id}") {
            val id = call.requireUUID()
            val storeId = call.requireUUID("storeId")
            val drink = drinkService.getDrinkById(id, storeId)
            if (drink == null) {
                call.respond(message = "Drink not found", status = HttpStatusCode.NotFound)
                return@get
            }
            call.respond(drink)

        }

        post {
            val storeId = call.requireUUID("storeId")
            val drink = call.receive<CreateDrinkRequest>()
            val createdDrink = drinkService.createDrink(drink, storeId)
            call.respond(message = createdDrink, status = HttpStatusCode.Created)
        }

        delete("/{id}") {
            val storeId = call.requireUUID("storeId")
            val id = call.requireUUID()
            drinkService.deleteDrink(id, storeId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}