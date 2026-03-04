package configuration

import de.okan.drink_and_snack_api.configuration.UUIDSerializer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.util.UUID


suspend fun ApplicationTestBuilder.runTestApplication(block: suspend (HttpClient) -> Unit) {
    val client = createClient {
        install(ContentNegotiation) {
            json(
                Json {
                    serializersModule = SerializersModule {
                        contextual(UUID::class, UUIDSerializer)
                    }
                }
            )
        }
    }

    block(client)
}
