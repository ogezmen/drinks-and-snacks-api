package de.okan.drink_and_snack_api.user.service

import de.okan.drink_and_snack_api.de.okan.drink_and_snack_api.user.api.model.RoleDTO
import de.okan.drink_and_snack_api.user.api.model.UserDTO
import de.okan.drink_and_snack_api.user.domain.Role
import de.okan.drink_and_snack_api.user.domain.User

fun Role.toDTO() = when(this) {
    Role.SELLER -> RoleDTO.SELLER
    Role.ADMIN -> RoleDTO.ADMIN
}

fun User.toDTO() = UserDTO(
    username = username,
    firstName = firstName,
    lastName = lastName,
    roles = roles.map { it.toDTO() }.toSet()
)