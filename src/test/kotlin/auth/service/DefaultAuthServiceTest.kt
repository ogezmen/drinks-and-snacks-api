package auth.service

import de.okan.drink_and_snack_api.auth.api.model.LoginRequest
import de.okan.drink_and_snack_api.auth.api.model.RegisterRequest
import de.okan.drink_and_snack_api.auth.domain.User
import de.okan.drink_and_snack_api.auth.persistence.UserRepository
import de.okan.drink_and_snack_api.auth.service.DefaultAuthService
import de.okan.drink_and_snack_api.auth.service.JwtService
import de.okan.drink_and_snack_api.auth.service.PasswordService
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

        every { userRepository.findByUsername(any()) } returns null
        every { passwordService.encrypt(any()) } returns "encryptedPassword"
        every { jwtService.generateAccessToken(any()) } returns "accessToken"
        every { userRepository.save(any()) } returns User(
            id = randomId,
            username = registerRequest.username,
            passwordHash = "encryptedPassword",
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
        )

        val sessionDTO = authService.register(registerRequest)

        verify { userRepository.findByUsername(registerRequest.username) }
        verify { passwordService.encrypt(registerRequest.password) }
        verify { userRepository.save(any()) }
        verify { jwtService.generateAccessToken(randomId) }

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
        )

        every { userRepository.findByUsername(any()) } returns user
        every { passwordService.checkPassword(any(), any())} returns true
        every { jwtService.generateAccessToken(any()) } returns "accessToken"

        val sessionDTO = authService.login(loginRequest)

        verify { userRepository.findByUsername(loginRequest.username) }
        verify { jwtService.generateAccessToken(user.id) }

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
        )

        every { userRepository.findByUsername(any()) } returns user
        every { passwordService.checkPassword(any(), any())} returns false


        authService.login(loginRequest)
    }
}