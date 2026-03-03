plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kover)
    kotlin("plugin.serialization") version "2.0.0"
}

group = "de.okan.drink_and_snack_api"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "de.okan.drink_and_snack_api.ApplicationKt*",
                    "de.okan.drink_and_snack_api.RoutingKt*",
                    "*.configuration.*",
                    "*.model.*",
                )
            }
        }

        verify {
            rule {
                bound {
                    minValue = 90
                }
            }
        }
    }
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.status.pages)

    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)

    implementation(libs.jbcrypt)

    implementation(libs.ktor.server.openapi)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.routing.openapi)

    implementation(libs.ktor.server.config.yaml)

    implementation(libs.logback.classic)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)

    implementation(libs.koin.ktor)
    implementation(libs.h2)

    implementation(libs.flyway.core)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.mockk)
}
