package user.persistence

import configuration.setupTestDatabase
import de.okan.drink_and_snack_api.user.domain.User
import de.okan.drink_and_snack_api.user.persistence.ExposedUserRepository
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExposedUsersRepositoryTest {

    private lateinit var repository: ExposedUserRepository

    @BeforeTest
    fun setup() {
        val database = setupTestDatabase()

        repository = ExposedUserRepository(database)
    }

    @Test
    fun `should return all users`() {
        val user1 = User(
            id = UUID.randomUUID(),
            username = "username",
            passwordHash = "password".reversed(),
            firstName = "firstName",
            lastName = "lastName",
        )

        val user2 = User(
            id = UUID.randomUUID(),
            username = "username2",
            passwordHash = "password2".reversed(),
            firstName = "firstName2",
            lastName = "lastName2",
        )

        repository.create(user1)
        repository.create(user2)

        val users = repository.findAll()

        assertEquals(2, users.size)
        assertEquals(user1, users[0])
        assertEquals(user2, users[1])
    }

    @Test
    fun `save and find by username and id`() {

        val id = UUID.randomUUID()

        val user = User(
            id = id,
            username = "testUser",
            passwordHash = "testPasswordHash",
            firstName = "testUserFirstName",
            lastName = "testUserLastName",
        )

        val createdUser = repository.create(user)

        val foundUser1 = repository.findByUsername("testUser")
        val foundUser2 = repository.findById(id)

        assertEquals(foundUser1, foundUser2)
        assertEquals(createdUser, foundUser1)
    }

    @Test
    fun `should delete by id`() {

        val id = UUID.randomUUID()

        val user = User(
            id = id,
            username = "testUser",
            passwordHash = "testPasswordHash",
            firstName = "testUserFirstName",
            lastName = "testUserLastName",
        )

        repository.create(user)

        repository.deleteById(id)

        val foundUser = repository.findById(id)

        assertNull(foundUser)
    }
}