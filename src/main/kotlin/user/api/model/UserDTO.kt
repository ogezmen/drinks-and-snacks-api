package de.okan.drink_and_snack_api.user.api.model

import de.okan.drink_and_snack_api.de.okan.drink_and_snack_api.user.api.model.RoleDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val username: String,
    val firstName: String,
    val lastName: String,
    val roles: Set<RoleDTO>,
)
