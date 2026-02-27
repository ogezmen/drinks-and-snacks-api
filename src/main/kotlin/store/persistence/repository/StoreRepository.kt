package com.example.store.persistence.repository

import com.example.store.persistence.entity.Store
import java.util.UUID

interface StoreRepository {
    fun findAll(): List<Store>
    fun findById(id: UUID): Store?
    fun save(store: Store): Store
    fun deleteById(id: UUID)
}