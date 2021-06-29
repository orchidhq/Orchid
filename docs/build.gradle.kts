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
    ModuleGroups.all.map { orchidRuntimeOnly(project(it.path)) }
}

// Orchid setup
// ---------------------------------------------------------------------------------------------------------------------

orchid {
    githubToken = project.properties["githubToken"]?.toString() ?: ""
}

val build by tasks
val check by tasks
val orchidBuild by tasks
val orchidServe by tasks

build.dependsOn(orchidBuild)
orchidBuild.mustRunAfter(check)
orchidBuild.dependsOn(ModuleGroups.all.tasksNamed("assemble"))
orchidServe.dependsOn(ModuleGroups.all.tasksNamed("assemble"))
