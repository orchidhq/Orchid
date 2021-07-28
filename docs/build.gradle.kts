plugins {
    java
    kotlin("jvm")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    id("com.eden.orchidPlugin")
}

dependencies {
    // generate own documentation with Orchid
    orchidImplementation(project(":bundles:orchid-all-bundle"))
}

// Orchid setup
// ---------------------------------------------------------------------------------------------------------------------
orchid {
    version = Config.projectVersion(project).documentationVersion
    diagnose = true
}

val build by tasks
val check by tasks
val orchidBuild by tasks
val orchidServe by tasks

build.dependsOn(orchidBuild)
