package de.okan.drink_and_snack_api.configuration.model

data class DatabaseConfiguration(
    val url: String,
    val driver: String,
    val username: String,
    val password: String,
)
