package de.okan.drink_and_snack_api.user.service

import de.okan.drink_and_snack_api.user.api.model.UserDTO
import de.okan.drink_and_snack_api.user.domain.User

fun User.toDTO() = UserDTO(
    username = username,
    firstName = firstName,
    lastName = lastName,
)