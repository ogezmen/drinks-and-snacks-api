package de.okan.drink_and_snack_api.auth.service

import de.okan.drink_and_snack_api.auth.api.model.LoginRequest
import de.okan.drink_and_snack_api.auth.api.model.RegisterRequest
import de.okan.drink_and_snack_api.auth.api.model.SessionDTO
import de.okan.drink_and_snack_api.auth.domain.User
import de.okan.drink_and_snack_api.auth.persistence.UserRepository
import java.util.UUID

class DefaultAuthService(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
    private val jwtService: JwtService,
) : AuthService {

    override fun register(registerRequest: RegisterRequest): SessionDTO {

        require(userRepository.findByUsername(registerRequest.username) == null) {
            throw IllegalArgumentException("Username already exists")
        }

        val user = User(
            id = UUID.randomUUID(),
            username = registerRequest.username,
            passwordHash = passwordService.encrypt(registerRequest.password),
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
        )

        val createdUser = userRepository.save(user)

        val accessToken = jwtService.generateAccessToken(createdUser.id)

        return SessionDTO(
            accessToken = accessToken,
        )
    }

    override fun login(loginRequest: LoginRequest): SessionDTO {
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw IllegalArgumentException("Invalid username or password")

        require(passwordService.checkPassword(loginRequest.password, user.passwordHash)) {
            throw IllegalArgumentException("Invalid username or password")
        }

        val accessToken = jwtService.generateAccessToken(user.id)

        return SessionDTO(
            accessToken = accessToken,
        )
    }
}