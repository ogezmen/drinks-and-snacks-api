package de.okan.drink_and_snack_api.auth.api

import de.okan.drink_and_snack_api.auth.api.model.LoginRequest
import de.okan.drink_and_snack_api.auth.api.model.RegisterRequest
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

// TODO: Implement the auth routes and connect them to the AuthService
fun Route.authRoutes() {
    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
        }

        post("/refresh") {
            val request = call.receive<LoginRequest>()
        }

        post("/logout") {

        }
    }
}