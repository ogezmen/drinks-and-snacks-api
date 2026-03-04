package de.okan.drink_and_snack_api.configuration

import de.okan.drink_and_snack_api.configuration.model.JwtConfigurationProperties
import de.okan.drink_and_snack_api.auth.service.AuthService
import de.okan.drink_and_snack_api.auth.service.BCryptPasswordService
import de.okan.drink_and_snack_api.auth.service.DefaultAuthService
import de.okan.drink_and_snack_api.auth.service.DefaultJwtService
import de.okan.drink_and_snack_api.auth.service.JwtService
import de.okan.drink_and_snack_api.auth.service.PasswordService
import de.okan.drink_and_snack_api.configuration.model.DatabaseConfigurationProperties
import de.okan.drink_and_snack_api.drink.persistence.DrinkRepository
import de.okan.drink_and_snack_api.drink.persistence.ExposedDrinkRepository
import de.okan.drink_and_snack_api.drink.service.DefaultDrinkService
import de.okan.drink_and_snack_api.drink.service.DrinkService
import de.okan.drink_and_snack_api.store.persistence.ExposedStoreRepository
import de.okan.drink_and_snack_api.store.persistence.StoreRepository
import de.okan.drink_and_snack_api.store.service.DefaultStoreService
import de.okan.drink_and_snack_api.store.service.StoreService
import de.okan.drink_and_snack_api.user.persistence.ExposedUserRepository
import de.okan.drink_and_snack_api.user.persistence.UserRepository
import de.okan.drink_and_snack_api.user.service.DefaultUserService
import de.okan.drink_and_snack_api.user.service.UserService
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

fun databaseConfigurationModule(applicationConfig: ApplicationConfig) = module {
    val databaseConfigurationProperties = DatabaseConfigurationProperties(
        url = applicationConfig.property("db.url").getString(),
        driver = applicationConfig.property("db.driver").getString(),
        username = applicationConfig.property("db.username").getString(),
        password = applicationConfig.property("db.password").getString(),
    )

    single<DatabaseConfigurationProperties> { databaseConfigurationProperties}
}

val databaseModule = module {
    single<Database> {
        Database.connect(
            url = get<DatabaseConfigurationProperties>().url,
            driver = get<DatabaseConfigurationProperties>().driver,
            user = get<DatabaseConfigurationProperties>().username,
            password = get<DatabaseConfigurationProperties>().password,
        )
    }
}

fun jwtConfigurationModule(applicationConfig: ApplicationConfig) = module {
    single<JwtConfigurationProperties> {
        JwtConfigurationProperties(
            secret = applicationConfig.property("auth.jwt.secret").getString(),
            issuer = applicationConfig.property("auth.jwt.issuer").getString(),
            audience = applicationConfig.property("auth.jwt.audience").getString(),
        )
    }
}

val authModule = module {
    single<UserRepository> { ExposedUserRepository(database = get()) }
    single<PasswordService> { BCryptPasswordService() }
    single<JwtService> { DefaultJwtService(jwtConfigurationProperties = get()) }
    single<AuthService> { DefaultAuthService(
        userRepository = get(),
        passwordService = get(),
        jwtService = get(),
    )}
}

val storeModule = module {
    single<StoreRepository> { ExposedStoreRepository(database = get()) }
    single<StoreService> { DefaultStoreService(storeRepository = get()) }
}

val drinkModule = module {
    single<DrinkRepository> { ExposedDrinkRepository(database = get()) }
    single<DrinkService> { DefaultDrinkService(drinkRepository = get()) }
}

val userModule = module {
    single<UserRepository> { ExposedUserRepository(database = get()) }
    single<UserService> { DefaultUserService(userRepository = get(), passwordService = get()) }
}