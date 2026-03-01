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
        val passwordHash1 = passwordService.hash("password")
        val passwordHash2 = passwordService.hash("password")

        assertNotEquals(passwordHash1, passwordHash2)

        assertTrue(passwordService.matches("password", passwordHash1))
        assertTrue(passwordService.matches("password", passwordHash2))
        assertFalse(passwordService.matches("wrong_password", passwordHash1))
    }
}