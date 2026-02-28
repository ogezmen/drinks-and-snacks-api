package de.okan.drink_and_snack_api.store.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateStoreRequest(
    val name: String,
)
