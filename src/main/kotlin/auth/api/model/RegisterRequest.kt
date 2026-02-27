package de.okan.drink_and_snack_api.auth.api.model

data class RegisterRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)
