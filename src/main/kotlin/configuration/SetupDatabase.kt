package de.okan.drinks_and_snacks_api.configuration

import de.okan.drinks_and_snacks_api.configuration.model.DatabaseConfigurationProperties
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