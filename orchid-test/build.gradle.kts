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
    api(moduleLibs.core)
    api(testLibs.bundles.all)
    testImplementation(moduleLibs.features.pages)
}
