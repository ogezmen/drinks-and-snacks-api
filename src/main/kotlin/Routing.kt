package com.example

import com.example.drink.repository.ExposedDrinkRepository
import com.example.drink.service.SimpleDrinkService
import com.example.store.routes.storeRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureRouting() {



    routing {
        get("/") {
            call.respondText("I'm alive!")
        }


        val database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver"
        )

        val drinkRepository = ExposedDrinkRepository(database)
        val drinkService = SimpleDrinkService(drinkRepository)

        storeRoutes(drinkService)
    }
}
