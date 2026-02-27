package com.example.drink

import com.example.drink.service.DrinkService
import com.example.drink.service.model.DrinkDTO
import com.example.getIdAsUUID
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
            call.respond(drinkService.getAllDrinks())
        }

        get("/{id}") {
            val uuid = call.getIdAsUUID()
            if (uuid == null) {
                call.respond(message = "Missing or malformed id", status = HttpStatusCode.BadRequest)
                return@get
            }
            val drink = drinkService.getDrinkById(uuid)
            if (drink == null) {
                call.respond(message = "Drink not found", status = HttpStatusCode.NotFound)
                return@get
            }
            call.respond(drink)

        }

        post {
            val drink = call.receive<DrinkDTO>()
            val createdDrink = drinkService.addDrink(drink)
            call.respond(message = createdDrink, status = HttpStatusCode.Created)
        }

        delete("/{id}") {
            val uuid = call.getIdAsUUID()
            if (uuid == null) {
                call.respond(message = "Missing or malformed id", status = HttpStatusCode.BadRequest)
                return@delete
            }
            // TODO: Implement delete logic using uuid
        }
    }
}