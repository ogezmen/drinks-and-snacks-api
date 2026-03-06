package de.okan.drinks_and_snacks_api.auth.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SessionDTO(
    val accessToken: String,
)
