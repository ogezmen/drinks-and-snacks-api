package de.okan.drink_and_snack_api

import de.okan.drink_and_snack_api.di.databaseModule
import de.okan.drink_and_snack_api.drink.di.drinkModule
import de.okan.drink_and_snack_api.drink.service.DefaultDrinkService
import de.okan.drink_and_snack_api.store.di.storeModule
import de.okan.drink_and_snack_api.store.service.DefaultStoreService
import io.ktor.server.application.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(Koin) {
        modules(databaseModule, storeModule, drinkModule)
    }

    val drinkService by inject<DefaultDrinkService>()
    val storeService by inject<DefaultStoreService>()

    configureRouting(
        storeService = storeService,
        drinkService = drinkService,
    )
}
