package de.okan.drink_and_snack_api.configuration

import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

fun databaseModule(applicationConfig: ApplicationConfig) = module {
    single<Database> {
        Database.connect(
            url = applicationConfig.property("db.url").getString(),
            driver = applicationConfig.property("db.driver").getString(),
            user = applicationConfig.property("db.username").getString(),
            password = applicationConfig.property("db.password").getString(),
        )
    }
}