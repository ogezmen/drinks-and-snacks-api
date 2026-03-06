package de.okan.drinks_and_snacks_api.configuration.model

data class JwtConfigurationProperties(
    val secret: String,
    val issuer: String,
    val audience: String,
)