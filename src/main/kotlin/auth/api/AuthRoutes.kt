package de.okan.drink_and_snack_api.auth.api

import de.okan.drink_and_snack_api.auth.api.model.LoginRequest
import de.okan.drink_and_snack_api.auth.api.model.RegisterRequest
import de.okan.drink_and_snack_api.auth.service.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(authService: AuthService) {
    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()

            val sessionDTO = authService.register(request)

            call.respond(message = sessionDTO, status = HttpStatusCode.Created)
        }

        post("/login") {
            val request = call.receive<LoginRequest>()

            val sessionDTO = authService.login(request)

            call.respond(message = sessionDTO, status = HttpStatusCode.OK)
        }
    }
}