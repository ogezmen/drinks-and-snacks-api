package de.okan.drink_and_snack_api.auth.service

import java.util.UUID

/**
 * Service responsible for generating and validating JSON Web Tokens (JWT)
 * used for authentication and authorization.
 */
interface JwtService {

    /**
     * Generates a signed access token for the given user.
     *
     * @param userId the unique identifier of the authenticated user
     *
     * @return a signed JWT access token
     */
    fun generateAccessToken(userId: String, roles: Set<String>): String

    /**
     * Validates the given access token.
     *
     * @param token the JWT to validate
     *
     * @return the user ID extracted from the given token, or `null` if the token is invalid or expired
     */
    fun validateAccessToken(token: String): UUID?
}