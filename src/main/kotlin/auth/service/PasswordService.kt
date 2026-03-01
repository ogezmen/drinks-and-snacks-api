package de.okan.drink_and_snack_api.auth.service

/**
 * Service responsible for securely hashing and verifying user passwords.
 */
interface PasswordService {

    /**
     * Creates a secure hash of the given plain text password.
     *
     * @param plainText the raw password provided by the user
     *
     * @return the generated password hash
     */
    fun hash(plainText: String): String

    /**
     * Verifies whether a plain text password matches a previously generated hash.
     *
     * @param plainText the raw password to verify
     * @param hash the stored hash to compare against
     *
     * @return `true` if the password matches the hash, otherwise `false`
     */
    fun matches(plainText: String, hash: String): Boolean
}