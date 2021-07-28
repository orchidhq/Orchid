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

    implementation(libs.kodiak.common.models)
    implementation(libs.kodiak.common.formatter)
    implementation(libs.kodiak.common.runner)

    testImplementation(Modules.OrchidJavadoc)
    testImplementation(Modules.OrchidGroovydoc)
    testImplementation(Modules.OrchidKotlindoc)
    testImplementation(Modules.OrchidSwiftdoc)
    testImplementation(Modules.OrchidPluginDocs)
}
