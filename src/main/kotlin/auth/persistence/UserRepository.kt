package de.okan.drink_and_snack_api.auth.persistence

import de.okan.drink_and_snack_api.auth.configuration.domain.User

interface UserRepository {
    fun findByUsername(username: String): User?
    fun save(user: User): User
}