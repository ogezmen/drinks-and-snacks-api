package auth.persistence

import de.okan.drink_and_snack_api.auth.configuration.domain.User
import de.okan.drink_and_snack_api.auth.persistence.ExposedUserRepository
import org.jetbrains.exposed.sql.Database
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExposedUsersRepositoryTest {

    private lateinit var repository: ExposedUserRepository

    @BeforeTest
    fun setup() {
        val uniqueDbName = "test_${UUID.randomUUID()}"
        val database = Database.connect(
            "jdbc:h2:mem:$uniqueDbName;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver",
        )

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

        val createdUser = repository.save(user)

        val foundUser = repository.findByUsername("testUser")

        assertEquals(createdUser, foundUser)
    }
}