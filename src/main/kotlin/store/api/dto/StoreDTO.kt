package de.okan.drinks_and_snacks_api.store.api.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class StoreDTO(
    @Contextual val id: UUID,
    val name: String,
)
