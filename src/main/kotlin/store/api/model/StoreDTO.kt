package de.okan.drink_and_snack_api.store.service.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class StoreDTO(
    @Contextual val id: UUID,
    val name: String,
)
