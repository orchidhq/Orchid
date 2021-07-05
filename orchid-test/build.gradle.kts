plugins {
    java
    kotlin("jvm")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
    `orchid-main-projects`
}

dependencies {
    api(Modules.OrchidCore)
    api("org.hamcrest:hamcrest-library:2.2") // remove this
    api("io.strikt:strikt-core:0.31.0")
    api("org.mockito:mockito-core:3.11.2")
    api("org.junit.jupiter:junit-jupiter-api:5.7.2")
    api("org.junit.jupiter:junit-jupiter-params:5.7.2")
    api("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")

    testImplementation(Modules.OrchidPages)
}
