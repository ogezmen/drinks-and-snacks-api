package de.okan.drinks_and_snacks_api.auth.service

import de.okan.drinks_and_snacks_api.auth.api.dto.LoginRequest
import de.okan.drinks_and_snacks_api.auth.api.dto.RegisterRequest
import de.okan.drinks_and_snacks_api.auth.api.dto.SessionDTO

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