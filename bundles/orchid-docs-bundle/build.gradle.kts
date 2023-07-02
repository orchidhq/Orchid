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

    implementation(moduleLibs.themes.editorial)
    implementation(moduleLibs.features.pages)
    implementation(moduleLibs.features.wiki)
    implementation(moduleLibs.features.forms)
    implementation(moduleLibs.features.changelog)
    implementation(moduleLibs.features.search)
    implementation(moduleLibs.features.diagrams)
    implementation(moduleLibs.features.syntaxHighlighter)
}
