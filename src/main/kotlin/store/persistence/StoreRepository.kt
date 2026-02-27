package com.example.store.persistence

import com.example.store.domain.Store
import java.util.UUID

interface StoreRepository {
    fun findAll(): List<Store>
    fun findById(id: UUID): Store?
    fun save(store: Store): Store
    fun deleteById(id: UUID)
}