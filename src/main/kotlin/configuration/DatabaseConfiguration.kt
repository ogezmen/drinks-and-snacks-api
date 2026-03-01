package de.okan.drink_and_snack_api.configuration

import de.okan.drink_and_snack_api.configuration.model.DatabaseConfiguration
import io.ktor.server.config.ApplicationConfig
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

fun databaseConfigurationModule(applicationConfig: ApplicationConfig) = module {
    val databaseConfiguration = DatabaseConfiguration(
        url = applicationConfig.property("db.url").getString(),
        driver = applicationConfig.property("db.driver").getString(),
        username = applicationConfig.property("db.username").getString(),
        password = applicationConfig.property("db.password").getString(),
    )

    single<DatabaseConfiguration> { databaseConfiguration}
}

val databaseModule = module {
    single<Database> {
        Database.connect(
            url = get<DatabaseConfiguration>().url,
            driver = get<DatabaseConfiguration>().driver,
            user = get<DatabaseConfiguration>().username,
            password = get<DatabaseConfiguration>().password,
        )
    }
}

fun migrateDatabase(databaseConfiguration: DatabaseConfiguration) {
    Flyway
        .configure()
        .dataSource(
            databaseConfiguration.url,
            databaseConfiguration.username,
            databaseConfiguration.password
        )
        .load()
        .migrate()
}