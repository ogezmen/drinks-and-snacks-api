package com.example

import com.example.drink.repository.ExposedDrinkRepository
import com.example.drink.service.SimpleDrinkService
import com.example.store.persistence.repository.ExposedStoreRepository
import com.example.store.service.SimpleStoreService
import com.example.store.storeRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import java.util.UUID


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

        val storeRepository = ExposedStoreRepository(database)
        val storeService = SimpleStoreService(storeRepository)

        storeRoutes(
            storeService = storeService,
            drinkService = drinkService,
        )
    }
}

fun ApplicationCall.getIdAsUUID(paramName: String = "id"): UUID? {
    val id = parameters[paramName] ?: return null
    return try {
        UUID.fromString(id)
    } catch (_: IllegalArgumentException) {
        null
    }
}
