@file:Suppress("UnstableApiUsage")
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
    implementation(moduleLibs.core)
    testImplementation(moduleLibs.test)

    implementation(moduleLibs.features.changelog)
    implementation(moduleLibs.features.wiki)
}
