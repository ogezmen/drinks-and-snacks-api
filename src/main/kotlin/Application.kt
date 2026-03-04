package de.okan.drink_and_snack_api

import de.okan.drink_and_snack_api.configuration.*
import de.okan.drink_and_snack_api.configuration.model.DatabaseConfigurationProperties
import de.okan.drink_and_snack_api.configuration.model.JwtConfigurationProperties
import io.ktor.server.application.*
import io.ktor.server.routing.*
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

    val databaseConfigurationProperties by inject<DatabaseConfigurationProperties>()

    migrateDatabase(databaseConfigurationProperties)

    val jwtConfigurationProperties by inject<JwtConfigurationProperties>()

    authConfiguration(jwtConfigurationProperties)

    configureRouting()

    routing {
        setupRoutes()
    }
}
