package de.okan.drink_and_snack_api.auth.service

import de.okan.drink_and_snack_api.auth.api.model.LoginRequest
import de.okan.drink_and_snack_api.auth.api.model.RegisterRequest
import de.okan.drink_and_snack_api.auth.api.model.SessionDTO

interface AuthService {
    fun register(registerRequest: RegisterRequest): SessionDTO
    fun login(loginRequest: LoginRequest): SessionDTO
}