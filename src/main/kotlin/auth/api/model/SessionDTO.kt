package de.okan.drink_and_snack_api.auth.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SessionDTO(
    val accessToken: String,
)
