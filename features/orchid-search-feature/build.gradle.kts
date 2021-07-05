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

    testImplementation(Modules.OrchidSourceDoc)
    testImplementation(Modules.OrchidKotlindoc)
    testImplementation(Modules.OrchidPosts)
    testImplementation(Modules.OrchidPages)
    testImplementation(Modules.OrchidWiki)
}
