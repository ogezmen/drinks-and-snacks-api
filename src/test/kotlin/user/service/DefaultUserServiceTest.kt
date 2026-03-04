package user.service

import de.okan.drink_and_snack_api.auth.service.PasswordService
import de.okan.drink_and_snack_api.user.api.model.DeleteAccountRequest
import de.okan.drink_and_snack_api.user.domain.User
import de.okan.drink_and_snack_api.user.persistence.UserRepository
import de.okan.drink_and_snack_api.user.service.DefaultUserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DefaultUserServiceTest {

    val userRepository: UserRepository = mockk()
    val passwordService: PasswordService = mockk()
    val userService = DefaultUserService(userRepository, passwordService)

    @Test
    fun `should return user by id`() {
        val id = UUID.randomUUID()

        val user = User(
            id = id,
            username = "username",
            passwordHash = "password".reversed(),
            firstName = "firstName",
            lastName = "lastName",
        )

        every { userRepository.findById(id) } returns user

        val userDTO = userService.getUserById(id)

        assertNotNull(userDTO)
        assertEquals(user.username, userDTO.username)
        assertEquals(user.firstName, userDTO.firstName)
        assertEquals(user.lastName, userDTO.lastName)
    }

    @Test
    fun `should delete user by id`() {
        val id = UUID.randomUUID()
        val deleteAccountRequest = DeleteAccountRequest(
            password = "password"
        )

        val user = User(
            id = id,
            username = "user",
            passwordHash = "password".reversed(),
            firstName = "firstName",
            lastName = "lastName",
        )

        every { userRepository.findById(any()) } returns user
        every { passwordService.matches(any(), any())} returns true
        every { userRepository.deleteById(any()) } returns Unit

        userService.deleteUserById(id, deleteAccountRequest)

        verify { passwordService.matches("password", "password".reversed()) }
        verify { userRepository.deleteById(id) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should not delete user with password is wrong`() {
        val id = UUID.randomUUID()
        val deleteAccountRequest = DeleteAccountRequest(
            password = "password"
        )

        val user = User(
            id = id,
            username = "user",
            passwordHash = "password".reversed(),
            firstName = "firstName",
            lastName = "lastName",
        )

        every { userRepository.findById(any()) } returns user
        every { passwordService.matches(any(), any())} returns false

        userService.deleteUserById(id, deleteAccountRequest)
    }
}