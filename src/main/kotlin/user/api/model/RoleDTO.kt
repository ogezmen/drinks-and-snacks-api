package de.okan.drinks_and_snacks_api.user.api.model

import kotlinx.serialization.Serializable

@Serializable
enum class RoleDTO {
    SELLER,
    ADMIN,
}