package de.okan.drink_and_snack_api.auth.service

import de.okan.drink_and_snack_api.auth.api.model.LoginRequest
import de.okan.drink_and_snack_api.auth.api.model.RegisterRequest
import de.okan.drink_and_snack_api.auth.api.model.SessionDTO

/**
 * Application service responsible for handling authentication
 * and user registration use cases.
 */
interface AuthService {

    /**
     * Registers a new user account.
     *
     * @param registerRequest registration data
     *
     * @return a [SessionDTO] with session data
     */
    fun register(registerRequest: RegisterRequest): SessionDTO

    /**
     * Authenticates an existing user.
     *
     * @param loginRequest login credentials
     *
     * @return a [SessionDTO] with session data
     */
    fun login(loginRequest: LoginRequest): SessionDTO
}