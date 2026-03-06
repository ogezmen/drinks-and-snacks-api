package de.okan.drinks_and_snacks_api.configuration.model

data class DatabaseConfigurationProperties(
    val url: String,
    val driver: String,
    val username: String,
    val password: String,
)
