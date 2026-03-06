package de.okan.drinks_and_snacks_api.user.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val username: String,
    val firstName: String,
    val lastName: String,
    val roles: Set<RoleDTO>,
)
