package de.okan.drink_and_snack_api.auth.persistence

import de.okan.drink_and_snack_api.auth.domain.User

/**
 * Repository for managing [User] persistence.
 */
interface UserRepository {

    /**
     * Retrieves a [User] by their unique username.
     *
     * @param username the unique username of the user
     * @return the [User] if found, or `null` if no user exists with the given username
     */
    fun findByUsername(username: String): User?

    /**
     * Creates a new [User] in the underlying data source.
     *
     * @param user the [User] to be created
     *
     * @return the created [User]
     */
    fun create(user: User): User
}