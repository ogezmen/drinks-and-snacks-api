package de.okan.drink_and_snack_api.auth.api.model

data class SessionDTO(
    val token: String,
    val refreshToken: String,
)
