package de.okan.drink_and_snack_api.store.domain

import java.util.UUID

data class Store(
    val id: UUID,
    val name: String,
    val ownerUserId: UUID,
    val ownerUsername: String?,
)