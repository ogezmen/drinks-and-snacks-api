package de.okan.drinks_and_snacks_api.user.service

import de.okan.drinks_and_snacks_api.user.api.model.DeleteAccountRequest
import de.okan.drinks_and_snacks_api.user.api.model.UserDTO
import java.util.UUID

/**
 * Application service responsible for managing user-related use cases
 */
interface UserService {

    /**
     * Retrieves all users
     *
     * @return a [List] of [UserDTO] representing registered all users
     */
    fun getUsers(): List<UserDTO>

    /**
     * Retrieves a specific user by their ID
     *
     * @param id the unique identifier of the user
     *
     * @return the corresponding [UserDTO] if found, or `null` if no store exists with the given ID
     */
    fun getUserById(id: UUID): UserDTO?

    /**
     * Deletes a specific user by their ID
     *
     * @param id the unique identifier of the user to delete
     * @param deleteAccountRequest the data required for deleting the user
     */
    fun deleteUserById(id: UUID, deleteAccountRequest: DeleteAccountRequest)

    /**
     * Deletes a specific user by their ID
     *
     * @param id the unique identifier of the user to delete
     */
    fun deleteUserById(id: UUID)
}