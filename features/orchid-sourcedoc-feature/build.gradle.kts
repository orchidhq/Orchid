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

    implementation(libs.kodiak.common.models)
    implementation(libs.kodiak.common.formatter)
    implementation(libs.kodiak.common.runner)

    testImplementation(moduleLibs.features.javadoc)
    testImplementation(moduleLibs.features.groovydoc)
    testImplementation(moduleLibs.features.kotlindoc)
    testImplementation(moduleLibs.features.swiftdoc)
    testImplementation(moduleLibs.features.pluginDocs)
}
