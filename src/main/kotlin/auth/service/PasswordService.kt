package de.okan.drink_and_snack_api.auth.service

interface PasswordService {
    fun encrypt(plainText: String): String
    fun checkPassword(password: String, passwordHash: String): Boolean
}