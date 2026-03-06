package de.okan.drinks_and_snacks_api.user.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountRequest(
    val password: String
)
