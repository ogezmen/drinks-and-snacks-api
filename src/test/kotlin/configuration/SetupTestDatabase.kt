package configuration

import de.okan.drinks_and_snacks_api.configuration.migrateDatabase
import de.okan.drinks_and_snacks_api.configuration.model.DatabaseConfigurationProperties
import org.jetbrains.exposed.sql.Database
import java.util.UUID

fun setupTestDatabase(): Database {
    val uniqueDbName = "test_${UUID.randomUUID()}"

    val databaseConfig = DatabaseConfigurationProperties(
        url = "jdbc:h2:mem:$uniqueDbName;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver",
        username = "",
        password = "",
    )

    migrateDatabase(databaseConfig)

    return Database.connect(
        databaseConfig.url,
        databaseConfig.driver,
    )

}