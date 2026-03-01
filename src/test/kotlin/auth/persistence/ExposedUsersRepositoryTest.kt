package auth.persistence

import configuration.setupTestDatabase
import de.okan.drink_and_snack_api.auth.domain.User
import de.okan.drink_and_snack_api.auth.persistence.ExposedUserRepository
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExposedUsersRepositoryTest {

    private lateinit var repository: ExposedUserRepository

    @BeforeTest
    fun setup() {
        val database = setupTestDatabase()

        repository = ExposedUserRepository(database)
    }

    @Test
    fun `save and find by username`() {

        val user = User(
            id = UUID.randomUUID(),
            username = "testUser",
            passwordHash = "testPasswordHash",
            firstName = "testUserFirstName",
            lastName = "testUserLastName",
        )

        val createdUser = repository.create(user)

        val foundUser = repository.findByUsername("testUser")

        assertEquals(createdUser, foundUser)
    }
}