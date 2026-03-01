package de.okan.drink_and_snack_api.store.persistence

import de.okan.drink_and_snack_api.auth.persistence.UsersTable
import de.okan.drink_and_snack_api.store.domain.Store
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedStoreRepository(
    private val database: Database
) : StoreRepository {

    override fun findAll(): List<Store> = transaction(database) {
        (StoresTable innerJoin UsersTable).selectAll().map {
            Store(
                id = it[StoresTable.id],
                name = it[StoresTable.name],
                ownerUserId = it[StoresTable.ownerUserId],
                ownerUsername = it[UsersTable.username],
            )
        }
    }

    override fun findById(id: UUID): Store? = transaction(database) {
        (StoresTable innerJoin UsersTable).select { StoresTable.id eq id }.mapNotNull {
            Store(
                id = it[StoresTable.id],
                name = it[StoresTable.name],
                ownerUserId = it[StoresTable.ownerUserId],
                ownerUsername = it[UsersTable.username],
            )
        }.singleOrNull()
    }

    override fun create(store: Store): Store = transaction(database) {
        StoresTable.insert {
            it[StoresTable.id] = store.id
            it[StoresTable.name] = store.name
            it[StoresTable.ownerUserId] = store.ownerUserId
        }
        val username = findById(store.id)?.ownerUsername
        store.copy(ownerUsername = username)
    }

    override fun deleteById(id: UUID, ownerUserId: UUID): Unit = transaction(database) {
        StoresTable.deleteWhere { (StoresTable.id eq id) and (StoresTable.ownerUserId eq ownerUserId) }
    }
}