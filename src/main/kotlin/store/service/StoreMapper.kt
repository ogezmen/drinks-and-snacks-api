package de.okan.drink_and_snack_api.store.service

import de.okan.drink_and_snack_api.store.domain.Store
import de.okan.drink_and_snack_api.store.service.model.StoreDTO

fun Store.toDTO(): StoreDTO = StoreDTO(
    id = this.id,
    name = this.name,
)