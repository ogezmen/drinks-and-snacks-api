package configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.*

fun Application.authTestConfiguration(userId: UUID, roles: List<String> = listOf()) {
    install(Authentication) {
        provider("auth-jwt") {
            authenticate { context ->

                val token = JWT.create()
                    .withClaim("userId", userId.toString())
                    .withClaim("roles", roles)
                    .sign(Algorithm.HMAC256("test"))

                val jwtPrincipal = JWTPrincipal(
                    JWT.decode(token)
                )

                context.principal(jwtPrincipal)
            }
        }
    }
}