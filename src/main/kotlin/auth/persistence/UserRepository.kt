package de.okan.drink_and_snack_api.auth.persistence

import de.okan.drink_and_snack_api.auth.domain.User

interface UserRepository {
    fun findByUsername(username: String): User?
    fun create(user: User): User
}