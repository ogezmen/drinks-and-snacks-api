package de.okan.drink_and_snack_api

import de.okan.drink_and_snack_api.auth.configuration.authConfiguration
import de.okan.drink_and_snack_api.auth.configuration.authModule
import de.okan.drink_and_snack_api.auth.configuration.jwtConfigurationModule
import de.okan.drink_and_snack_api.auth.configuration.model.JwtConfiguration
import de.okan.drink_and_snack_api.configuration.databaseConfigurationModule
import de.okan.drink_and_snack_api.configuration.databaseModule
import de.okan.drink_and_snack_api.configuration.model.DatabaseConfiguration
import de.okan.drink_and_snack_api.configuration.migrateDatabase
import de.okan.drink_and_snack_api.drink.configuration.drinkModule
import de.okan.drink_and_snack_api.store.configuration.storeModule
import de.okan.drink_and_snack_api.user.configuration.userModule
import io.ktor.server.application.*
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(Koin) {
        modules(
            databaseConfigurationModule(environment.config),
            databaseModule,
            jwtConfigurationModule(environment.config),
            storeModule,
            drinkModule,
            authModule,
            userModule,
        )
    }

    val databaseConfiguration by inject<DatabaseConfiguration>()

    migrateDatabase(databaseConfiguration)

    val jwtConfiguration by inject<JwtConfiguration>()

    authConfiguration(jwtConfiguration)

    configureRouting()

    routing {
        setupRoutes()
    }
}
