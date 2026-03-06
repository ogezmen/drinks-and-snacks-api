package de.okan.drinks_and_snacks_api.auth.service

import org.mindrot.jbcrypt.BCrypt

class BCryptPasswordService : PasswordService {
    override fun hash(plainText: String): String {
        return BCrypt.hashpw(plainText, BCrypt.gensalt())
    }

    override fun matches(plainText: String, hash: String): Boolean {
        return BCrypt.checkpw(plainText, hash)
    }
}