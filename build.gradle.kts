plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "2.0.0"
    jacoco
}

group = "de.okan.drink_and_snack_api"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

val classesExcludedForTests = listOf(
    "/de/okan/drink_and_snack_api/ApplicationKt*",
    "**/configuration/**",
    "**/di/**",
    "**/model/**",
)

afterEvaluate {
    tasks.jacocoTestReport {
        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude(classesExcludedForTests)
                }
            })
        )
    }

    tasks.jacocoTestCoverageVerification {
        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude(classesExcludedForTests)
                }
            })
        )

        violationRules {

            rule {
                element = "CLASS"

                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = "0.90".toBigDecimal()
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

    implementation(libs.ktor.server.config.yaml)

    implementation(libs.logback.classic)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)

    implementation(libs.koin.ktor)
    implementation(libs.h2)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.mockk)
}
