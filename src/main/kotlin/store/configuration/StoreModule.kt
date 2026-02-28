package de.okan.drink_and_snack_api.store.configuration

import de.okan.drink_and_snack_api.store.persistence.ExposedStoreRepository
import de.okan.drink_and_snack_api.store.persistence.StoreRepository
import de.okan.drink_and_snack_api.store.service.DefaultStoreService
import de.okan.drink_and_snack_api.store.service.StoreService
import org.koin.dsl.module

val storeModule = module {
    single<StoreRepository> { ExposedStoreRepository(database = get()) }
    single<StoreService> { DefaultStoreService(storeRepository = get()) }
}