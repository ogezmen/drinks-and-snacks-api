package de.okan.drink_and_snack_api.user.domain

import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String,
    val roles: Set<Role>
)
