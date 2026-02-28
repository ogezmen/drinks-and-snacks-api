package de.okan.drink_and_snack_api.auth.configuration.model

data class JwtConfiguration(
    val secret: String,
    val issuer: String,
    val audience: String,
)
