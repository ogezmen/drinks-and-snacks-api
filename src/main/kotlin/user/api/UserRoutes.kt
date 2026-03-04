package de.okan.drink_and_snack_api.user.api

import de.okan.drink_and_snack_api.requireUserIDFromJWT
import de.okan.drink_and_snack_api.user.api.model.DeleteAccountRequest
import de.okan.drink_and_snack_api.user.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.setupUserRoutes(userService: UserService) {
    route("/api/v1/users") {
        authenticate("auth-jwt") {

            route("/me") {
                get {
                    val id = call.requireUserIDFromJWT()

                    val user = userService.getUserById(id) ?: throw NoSuchElementException("User with ID $id not found")

                    call.respond(user)
                }

                delete {
                    val id = call.requireUserIDFromJWT()
                    val deleteAccountRequest = call.receive<DeleteAccountRequest>()

                    userService.deleteUserById(id, deleteAccountRequest)

                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}