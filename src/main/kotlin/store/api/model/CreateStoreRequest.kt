package de.okan.drinks_and_snacks_api.store.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateStoreRequest(
    val name: String,
)
