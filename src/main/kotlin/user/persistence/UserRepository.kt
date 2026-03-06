package de.okan.drinks_and_snacks_api.user.persistence

import de.okan.drinks_and_snacks_api.user.domain.User
import java.util.UUID

/**
 * Repository for managing [User] persistence.
 */
interface UserRepository {

    /**
     * Retries every [User]
     *
     * @return a [List] with every [User]
     */
    fun findAll(): List<User>

    /**
     * Retrieves a [User] by their unique identifier
     *
     * @param id the user's unique identifier
     *
     * @return the [User] if found, or `null` if no user exists with the given identifier
     */
    fun findById(id: UUID): User?

    /**
     * Retrieves a [User] by their unique username.
     *
     * @param username the unique username of the user
     *
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

    /**
     * Deletes a [User] by their unique identifier
     *
     * @param id the user's unique identifier
     */
    fun deleteById(id: UUID)
}