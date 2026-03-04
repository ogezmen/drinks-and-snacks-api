package de.okan.drink_and_snack_api.configuration.model

data class JwtConfigurationProperties(
    val secret: String,
    val issuer: String,
    val audience: String,
)