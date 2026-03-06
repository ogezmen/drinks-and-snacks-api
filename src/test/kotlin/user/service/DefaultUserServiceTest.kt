package user.service

import de.okan.drinks_and_snacks_api.auth.service.PasswordService
import de.okan.drinks_and_snacks_api.user.api.dto.DeleteAccountRequest
import de.okan.drinks_and_snacks_api.user.domain.Role
import de.okan.drinks_and_snacks_api.user.domain.User
import de.okan.drinks_and_snacks_api.user.persistence.UserRepository
import de.okan.drinks_and_snacks_api.user.service.DefaultUserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DefaultUserServiceTest {

    val userRepository: UserRepository = mockk()
    val passwordService: PasswordService = mockk()
    val userService = DefaultUserService(userRepository, passwordService)

    @Test
    fun `should return all users`() {
        val user1 = User(
            id = UUID.randomUUID(),
            username = "test",
            passwordHash = "passwordHash".reversed(),
            firstName = "firstName",
            lastName = "lastName",
            roles = setOf(),
        )
        val user2 = User(
            id = UUID.randomUUID(),
            username = "test2",
            passwordHash = "passwordHash2".reversed(),
            firstName = "firstName2",
            lastName = "lastName2",
            roles = setOf(),
        )

        every { userRepository.findAll() } returns listOf(user1, user2)

        val users = userService.getUsers()

        assertEquals(2, users.size)
        assertEquals(user1.firstName, users[0].firstName)
        assertEquals(user2.firstName, users[1].firstName)
    }

    @Test
    fun `should return user by id`() {
        val id = UUID.randomUUID()

        val user = User(
            id = id,
            username = "username",
            passwordHash = "password".reversed(),
            firstName = "firstName",
            lastName = "lastName",
            roles = setOf(Role.SELLER, Role.ADMIN),
        )

        every { userRepository.findById(id) } returns user

        val userDTO = userService.getUserById(id)

        assertNotNull(userDTO)
        assertEquals(user.username, userDTO.username)
        assertEquals(user.firstName, userDTO.firstName)
        assertEquals(user.lastName, userDTO.lastName)
    }

    @Test
    fun `should not return user by id if user is not found`() {
        val id = UUID.randomUUID()

        every { userRepository.findById(id) } returns null

        val userDTO = userService.getUserById(id)

        assertNull(userDTO)
    }

    @Test
    fun `should delete user by id`() {
        val id = UUID.randomUUID()

        val user = User(
            id = id,
            username = "user",
            passwordHash = "password".reversed(),
            firstName = "firstName",
            lastName = "lastName",
            roles = setOf(),
        )

        every { userRepository.findById(any()) } returns user
        every { passwordService.matches(any(), any())} returns true
        every { userRepository.deleteById(any()) } returns Unit

        userService.deleteUserById(id)

        verify { userRepository.deleteById(id) }
    }

    @Test
    fun `should delete user by id if password is correct`() {
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
            roles = setOf(),
        )

        every { userRepository.findById(any()) } returns user
        every { passwordService.matches(any(), any())} returns true
        every { userRepository.deleteById(any()) } returns Unit

        userService.deleteUserById(id, deleteAccountRequest)

        verify { passwordService.matches("password", "password".reversed()) }
        verify { userRepository.deleteById(id) }
    }

    @Test(expected = NoSuchElementException::class)
    fun `should not delete user by id if user is not found`() {
        val id = UUID.randomUUID()
        val deleteAccountRequest = DeleteAccountRequest(
            password = "password"
        )

        every { userRepository.findById(any()) } returns null

        userService.deleteUserById(id, deleteAccountRequest)
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
            roles = setOf(),
        )

        every { userRepository.findById(any()) } returns user
        every { passwordService.matches(any(), any())} returns false

        userService.deleteUserById(id, deleteAccountRequest)

        verify(exactly = 1) { userRepository.deleteById(id) }
    }
}