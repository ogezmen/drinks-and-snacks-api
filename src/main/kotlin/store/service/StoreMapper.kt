package de.okan.drinks_and_snacks_api.store.service

import de.okan.drinks_and_snacks_api.store.domain.Store
import de.okan.drinks_and_snacks_api.store.api.dto.StoreDTO

fun Store.toDTO(): StoreDTO = StoreDTO(
    id = this.id,
    name = this.name,
)