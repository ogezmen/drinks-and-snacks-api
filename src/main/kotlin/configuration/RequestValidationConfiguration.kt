package de.okan.drinks_and_snacks_api.configuration

import de.okan.drinks_and_snacks_api.drink.api.dto.CreateDrinkRequest
import de.okan.drinks_and_snacks_api.drink.api.dto.DrinkDTO
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<CreateDrinkRequest> { request ->
            when {
                request.alcoholPercentage in 0.0..100.0 ->
                    ValidationResult.Invalid("Alcohol percentage must be between 0 and 100")
                else -> ValidationResult.Valid
            }
        }
        validate<DrinkDTO> { drink ->
            when {
                drink.alcoholPercentage in 0.0..100.0 ->
                    ValidationResult.Invalid("Alcohol percentage must be between 0 and 100")
                else -> ValidationResult.Valid
            }
        }
    }
}