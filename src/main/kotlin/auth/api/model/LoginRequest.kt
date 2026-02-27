package de.okan.drink_and_snack_api.auth.api.model

data class LoginRequest(
    val username: String,
    val password: String,
)
