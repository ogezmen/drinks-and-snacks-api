package auth.service

import de.okan.drink_and_snack_api.auth.service.BCryptPasswordService
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BCryptPasswordServiceTest {

    @Test
    fun `generate password`() {
        val passwordService = BCryptPasswordService()
        val passwordHash1 = passwordService.encrypt("password")
        val passwordHash2 = passwordService.encrypt("password")

        assertNotEquals(passwordHash1, passwordHash2)

        assertTrue(passwordService.checkPassword("password", passwordHash1))
        assertTrue(passwordService.checkPassword("password", passwordHash2))
        assertFalse(passwordService.checkPassword("wrong_password", passwordHash1))
    }
}