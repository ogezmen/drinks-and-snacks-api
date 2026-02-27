package com.example.drink.api.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class DrinkDTO(
    @Contextual val id: UUID? = null,
    val name: String,
)