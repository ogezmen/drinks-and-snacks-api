package de.okan.drink_and_snack_api.auth.service

import org.mindrot.jbcrypt.BCrypt

class BCryptPasswordService : PasswordService {
    override fun encrypt(plainText: String): String {
        return BCrypt.hashpw(plainText, BCrypt.gensalt())
    }

    override fun checkPassword(password: String, passwordHash: String): Boolean {
        return BCrypt.checkpw(password, passwordHash)
    }
}