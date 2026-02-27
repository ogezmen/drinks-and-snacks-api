package com.example.drink.api

import com.example.drink.service.DrinkService
import com.example.drink.api.model.DrinkDTO
import com.example.requireUUID
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
            val drink = call.receive<DrinkDTO>()
            val createdDrink = drinkService.addDrink(drink, storeId)
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