package de.okan.drink_and_snack_api.user.configuration

import de.okan.drink_and_snack_api.user.persistence.ExposedUserRepository
import de.okan.drink_and_snack_api.user.persistence.UserRepository
import de.okan.drink_and_snack_api.user.service.DefaultUserService
import de.okan.drink_and_snack_api.user.service.UserService
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> { ExposedUserRepository(database = get()) }
    single<UserService> { DefaultUserService(userRepository = get(), passwordService = get()) }
}