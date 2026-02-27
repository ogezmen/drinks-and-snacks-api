package com.example.store.service.mapper

import com.example.store.persistence.entity.Store
import com.example.store.service.model.StoreDTO

fun Store.toDTO(): StoreDTO = StoreDTO(
    id = this.id,
    name = this.name,
)