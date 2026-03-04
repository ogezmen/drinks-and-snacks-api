package de.okan.drink_and_snack_api.user.service

import de.okan.drink_and_snack_api.auth.service.PasswordService
import de.okan.drink_and_snack_api.user.api.model.DeleteAccountRequest
import de.okan.drink_and_snack_api.user.api.model.UserDTO
import de.okan.drink_and_snack_api.user.persistence.UserRepository
import java.util.UUID

class DefaultUserService(
    val userRepository: UserRepository,
    val passwordService: PasswordService
) : UserService {

    override fun getUserById(id: UUID): UserDTO? {
        return userRepository.findById(id)?.toDTO()
    }

    override fun deleteUserById(id: UUID, deleteAccountRequest: DeleteAccountRequest) {
        val user =  userRepository.findById(id) ?: throw NoSuchElementException("User not found")

        require(passwordService.matches(deleteAccountRequest.password, user.passwordHash)) {
            "Wrong password"
        }

        userRepository.deleteById(id)
    }
}