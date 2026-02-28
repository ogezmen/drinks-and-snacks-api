package de.okan.drink_and_snack_api

import de.okan.drink_and_snack_api.auth.configuration.authConfiguration
import de.okan.drink_and_snack_api.auth.configuration.authModule
import de.okan.drink_and_snack_api.auth.configuration.jwtConfigurationModule
import de.okan.drink_and_snack_api.auth.configuration.model.JwtConfiguration
import de.okan.drink_and_snack_api.configuration.databaseModule
import de.okan.drink_and_snack_api.drink.configuration.drinkModule
import de.okan.drink_and_snack_api.store.configuration.storeModule
import io.ktor.server.application.*
import org.koin.ktor.ext.getKoin
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(Koin) {
        modules(
            databaseModule(environment.config),
            jwtConfigurationModule(environment.config),
            storeModule,
            drinkModule,
            authModule,
        )
    }

    val jwtConfiguration by inject<JwtConfiguration>()

    authConfiguration(jwtConfiguration)

    configureRouting(
        storeService = getKoin().get(),
        drinkService = getKoin().get(),
        authService = getKoin().get(),
    )
}
