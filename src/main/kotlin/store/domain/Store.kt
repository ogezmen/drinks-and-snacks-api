package de.okan.drinks_and_snacks_api.store.domain

import java.util.UUID

data class Store(
    val id: UUID,
    val name: String,
    val ownerUserId: UUID,
    val ownerUsername: String? = null,
)