package com.example.store.service.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class StoreDTO(
    @Contextual val id: UUID,
    val name: String,
)
