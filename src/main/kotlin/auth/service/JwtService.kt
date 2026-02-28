package de.okan.drink_and_snack_api.auth.service

import java.util.UUID

interface JwtService {
    fun generateAccessToken(userId: UUID): String
    fun validateAccessToken(token: String): String?
}