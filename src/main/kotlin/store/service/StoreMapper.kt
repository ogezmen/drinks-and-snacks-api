package com.example.store.service

import com.example.store.domain.Store
import com.example.store.service.model.StoreDTO

fun Store.toDTO(): StoreDTO = StoreDTO(
    id = this.id,
    name = this.name,
)