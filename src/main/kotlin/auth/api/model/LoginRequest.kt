package de.okan.drinks_and_snacks_api.auth.api.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
)
