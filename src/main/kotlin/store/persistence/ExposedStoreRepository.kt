package de.okan.drink_and_snack_api.store.persistence

import de.okan.drink_and_snack_api.store.domain.Store
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedStoreRepository(
    private val database: Database
) : StoreRepository {

    init {
        transaction(database) {
            SchemaUtils.create(StoresTable)
        }
    }

    override fun findAll(): List<Store> = transaction(database) {
        StoresTable.selectAll().map {
            Store(
                id = it[StoresTable.id],
                name = it[StoresTable.name]
            )
        }
    }

    override fun findById(id: UUID): Store? = transaction(database) {
        StoresTable.select { StoresTable.id eq id }.mapNotNull {
            Store(
                id = it[StoresTable.id],
                name = it[StoresTable.name]
            )
        }.singleOrNull()
    }

    override fun save(store: Store): Store = transaction(database) {
        StoresTable.insert {
            it[id] = store.id
            it[name] = store.name
        }
        store
    }

    override fun deleteById(id: UUID): Unit = transaction(database) {
        StoresTable.deleteWhere { StoresTable.id eq id }
    }
}