package auth.service

import de.okan.drinks_and_snacks_api.auth.api.dto.LoginRequest
import de.okan.drinks_and_snacks_api.auth.api.dto.RegisterRequest
import de.okan.drinks_and_snacks_api.user.domain.User
import de.okan.drinks_and_snacks_api.user.persistence.UserRepository
import de.okan.drinks_and_snacks_api.auth.service.DefaultAuthService
import de.okan.drinks_and_snacks_api.auth.service.JwtService
import de.okan.drinks_and_snacks_api.auth.service.PasswordService
import de.okan.drinks_and_snacks_api.user.domain.Role
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAuthServiceTest {

    val userRepository = mockk<UserRepository>()
    val passwordService = mockk<PasswordService>()
    val jwtService = mockk<JwtService>()

    val authService: DefaultAuthService = DefaultAuthService(
        userRepository = userRepository,
        passwordService = passwordService,
        jwtService = jwtService,
    )

    @Test
    fun `register user`() {
        val registerRequest = RegisterRequest(
            username = "testuser",
            password = "password",
            firstName = "Test",
            lastName = "User",
        )

        val randomId = UUID.randomUUID()
        val roles = setOf(Role.SELLER)

        every { userRepository.findByUsername(any()) } returns null
        every { passwordService.hash(any()) } returns "encryptedPassword"
        every { jwtService.generateAccessToken(any(), any()) } returns "accessToken"
        every { userRepository.create(any()) } returns User(
            id = randomId,
            username = registerRequest.username,
            passwordHash = "encryptedPassword",
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            roles = roles,
        )

        val sessionDTO = authService.register(registerRequest)

        verify { userRepository.findByUsername(registerRequest.username) }
        verify { passwordService.hash(registerRequest.password) }
        verify { userRepository.create(any()) }
        verify { jwtService.generateAccessToken(
            userId = randomId.toString(),
            roles = roles.map { it.toString() }.toSet())
        }

        assertEquals("accessToken", sessionDTO.accessToken)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should fail registration with username is taken`() {
        val registerRequest = RegisterRequest(
            username = "testuser",
            password = "password",
            firstName = "Test",
            lastName = "User",
        )

        every { userRepository.findByUsername("testuser") } returns User(
            id = UUID.randomUUID(),
            username = registerRequest.username,
            passwordHash = "encryptedPassword",
            firstName = "Test",
            lastName = "User",
            roles = setOf(),
        )

        authService.register(registerRequest)
    }

    @Test
    fun `login user`() {
        val loginRequest = LoginRequest(
            username = "testuser",
            password = "password",
        )

        val user = User(
            id = UUID.randomUUID(),
            username = loginRequest.username,
            passwordHash = "encryptedPassword",
            firstName = "Test",
            lastName = "User",
            roles = setOf(),
        )

        every { userRepository.findByUsername(any()) } returns user
        every { passwordService.matches(any(), any())} returns true
        every { jwtService.generateAccessToken(any(), any()) } returns "accessToken"

        val sessionDTO = authService.login(loginRequest)

        verify { userRepository.findByUsername(loginRequest.username) }
        verify { jwtService.generateAccessToken(user.id.toString(), setOf()) }

        assertEquals("accessToken", sessionDTO.accessToken)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `login user wrong username`() {
        val loginRequest = LoginRequest(
            username = "testuser",
            password = "password",
        )

        every { userRepository.findByUsername(any()) } returns null


        authService.login(loginRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `login user wrong password`() {
        val loginRequest = LoginRequest(
            username = "testuser",
            password = "password",
        )

        val user = User(
            id = UUID.randomUUID(),
            username = loginRequest.username,
            passwordHash = "encryptedPassword",
            firstName = "Test",
            lastName = "User",
            roles = setOf(),
        )

        every { userRepository.findByUsername(any()) } returns user
        every { passwordService.matches(any(), any())} returns false


        authService.login(loginRequest)
    }
}