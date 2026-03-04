package de.okan.drink_and_snack_api.user.service

import de.okan.drink_and_snack_api.user.api.model.DeleteAccountRequest
import de.okan.drink_and_snack_api.user.api.model.UserDTO
import java.util.UUID

/**
 * Application service responsible for managing user-related use cases
 */
interface UserService {

    /**
     * Retrieves a specific user by their ID
     */
    fun getUserById(id: UUID): UserDTO?

    /**
     * Deletes a specific drink by their ID
     */
    fun deleteUserById(id: UUID, deleteAccountRequest: DeleteAccountRequest)
}