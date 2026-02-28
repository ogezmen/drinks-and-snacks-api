package de.okan.drink_and_snack_api.drink.di

import de.okan.drink_and_snack_api.drink.repository.DrinkRepository
import de.okan.drink_and_snack_api.drink.repository.ExposedDrinkRepository
import de.okan.drink_and_snack_api.drink.service.DefaultDrinkService
import de.okan.drink_and_snack_api.drink.service.DrinkService
import org.koin.dsl.module

val drinkModule = module {
    single<DrinkRepository> { ExposedDrinkRepository(database = get()) }
    single<DrinkService> { DefaultDrinkService(drinkRepository = get()) }
}