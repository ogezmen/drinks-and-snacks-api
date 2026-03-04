package de.okan.drink_and_snack_api.configuration

import de.okan.drink_and_snack_api.configuration.model.DatabaseConfigurationProperties
import org.flywaydb.core.Flyway

fun migrateDatabase(databaseConfigurationProperties: DatabaseConfigurationProperties) {
    Flyway
        .configure()
        .dataSource(
            databaseConfigurationProperties.url,
            databaseConfigurationProperties.username,
            databaseConfigurationProperties.password
        )
        .load()
        .migrate()
}