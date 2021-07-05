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

    implementation("io.github.copper-leaf:common-models:1.0.0")
    implementation("io.github.copper-leaf:common-formatter:1.0.0")
    implementation("io.github.copper-leaf:common-runner:1.0.0")

    testImplementation(Modules.OrchidJavadoc)
    testImplementation(Modules.OrchidGroovydoc)
    testImplementation(Modules.OrchidKotlindoc)
    testImplementation(Modules.OrchidSwiftdoc)
    testImplementation(Modules.OrchidPluginDocs)
}
