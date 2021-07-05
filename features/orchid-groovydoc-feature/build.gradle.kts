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
    implementation(Modules.OrchidCore)
    testImplementation(Modules.OrchidTest)

    implementation(Modules.OrchidSourceDoc)
    implementation("io.github.copper-leaf:groovydoc-runner:1.0.0")
}
