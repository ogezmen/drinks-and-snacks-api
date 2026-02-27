package com.example.store.service

import com.example.store.api.model.CreateStoreRequest
import com.example.store.service.model.StoreDTO
import java.util.UUID

interface StoreService {
    fun getAllStores(): List<StoreDTO>
    fun getStoreById(id: UUID): StoreDTO?
    fun createStore(store: CreateStoreRequest): StoreDTO
    fun deleteStore(id: UUID)
}