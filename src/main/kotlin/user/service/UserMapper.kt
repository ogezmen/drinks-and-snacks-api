package de.okan.drinks_and_snacks_api.user.service

import de.okan.drinks_and_snacks_api.user.api.dto.RoleDTO
import de.okan.drinks_and_snacks_api.user.api.dto.UserDTO
import de.okan.drinks_and_snacks_api.user.domain.Role
import de.okan.drinks_and_snacks_api.user.domain.User

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