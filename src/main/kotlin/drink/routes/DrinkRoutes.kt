package com.example.drink.routes

import com.example.drink.service.DrinkService
import com.example.drink.service.model.DrinkDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.drinkRoutes(drinkService: DrinkService) {
    route("/drinks") {
        get {
            call.respond(drinkService.getAllDrinks())
        }

        get("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(message = "Missing or malformed id", status = HttpStatusCode.BadRequest)
                return@get
            }
            val uuid = try {
                java.util.UUID.fromString(id)
            } catch (e: IllegalArgumentException) {
                call.respond(message = "Invalid UUID format", status = HttpStatusCode.BadRequest)
                return@get
            }
            val drink = drinkService.getDrinkById(uuid)
            if (drink == null) {
                call.respond(message = "Drink not found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(drink)
            }
        }
        post {
            val drink = call.receive<DrinkDTO>()
            val createdDrink = drinkService.addDrink(drink)
            call.respond(message = createdDrink, status = HttpStatusCode.Created)
        }
    }
}