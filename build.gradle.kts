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
    "/de/okan/drink_and_snack_api/configuration/**",
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



val exposedVersion = "0.42.0"
val h2Version = "2.2.222"
val mockkVersion = "1.13.8"

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-status-pages")
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.config.yaml)
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    implementation("com.h2database:h2:$h2Version")
    testImplementation(libs.ktor.server.test.host)
    testImplementation("io.ktor:ktor-client-core")
    testImplementation("io.ktor:ktor-client-content-negotiation")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json")
    testImplementation(libs.kotlin.test.junit)
    testImplementation("io.mockk:mockk:$mockkVersion")
}
