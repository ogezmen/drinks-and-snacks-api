package de.okan.drinks_and_snacks_api.user.domain

import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String,
    val roles: Set<Role>
)
