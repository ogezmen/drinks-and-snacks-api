package de.okan.drink_and_snack_api

import de.okan.drink_and_snack_api.drink.repository.ExposedDrinkRepository
import de.okan.drink_and_snack_api.drink.service.DefaultDrinkService
import de.okan.drink_and_snack_api.store.persistence.ExposedStoreRepository
import de.okan.drink_and_snack_api.store.service.DefaultStoreService
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver"
    )

    val drinkRepository = ExposedDrinkRepository(database)
    val drinkService = DefaultDrinkService(drinkRepository)

    val storeRepository = ExposedStoreRepository(database)
    val storeService = DefaultStoreService(storeRepository)

    configureRouting(
        storeService = storeService,
        drinkService = drinkService,
    )
}
