package de.okan.drink_and_snack_api.user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val username: String,
    val firstName: String,
    val lastName: String,
)
