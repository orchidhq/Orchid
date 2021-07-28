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
    api(testLibs.bundles.all)
    testImplementation(Modules.OrchidPages)
}
