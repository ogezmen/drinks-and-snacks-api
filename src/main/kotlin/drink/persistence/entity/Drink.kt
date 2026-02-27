package com.example.drink.repository.entity

import java.util.UUID

data class Drink(
    val id: UUID,
    val name: String,
    val storeId: UUID,
)