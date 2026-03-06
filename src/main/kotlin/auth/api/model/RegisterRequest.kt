package de.okan.drinks_and_snacks_api.auth.api.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)
