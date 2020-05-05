plugins {
    id("com.eden.orchidPlugin")
}

version = rootProject.version

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx")
}

dependencies {
    // generate own documentation with Orchid
    orchidRuntimeOnly(ModuleGroups.all)
}

// Orchid setup
//----------------------------------------------------------------------------------------------------------------------

val build by tasks
val check by tasks
val orchidBuild by tasks
val orchidServe by tasks

build.dependsOn(orchidBuild)
orchidBuild.mustRunAfter(check)
orchidBuild.dependsOn(*ModuleGroups.all.tasksNamed("assemble"))
orchidServe.dependsOn(*ModuleGroups.all.tasksNamed("assemble"))
