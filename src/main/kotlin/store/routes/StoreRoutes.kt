package com.example.store.routes

import com.example.drink.routes.drinkRoutes
import com.example.drink.service.DrinkService
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.storeRoutes(drinkService: DrinkService) {
    route("/store") {

        route("/{storeId}") {
            drinkRoutes(drinkService)
        }
    }
}