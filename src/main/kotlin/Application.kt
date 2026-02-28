package de.okan.drink_and_snack_api

import de.okan.drink_and_snack_api.configuration.databaseModule
import de.okan.drink_and_snack_api.drink.configuration.drinkModule
import de.okan.drink_and_snack_api.drink.service.DrinkService
import de.okan.drink_and_snack_api.store.configuration.storeModule
import de.okan.drink_and_snack_api.store.service.StoreService
import io.ktor.server.application.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(Koin) {
        modules(
            databaseModule(environment.config),
            storeModule,
            drinkModule,
        )
    }

    val drinkService by inject<DrinkService>()
    val storeService by inject<StoreService>()

    configureRouting(
        storeService = storeService,
        drinkService = drinkService,
    )
}
