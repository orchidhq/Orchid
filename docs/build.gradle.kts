plugins {
    id("com.eden.orchidPlugin")
}
apply(from = "$rootDir/gradle/actions/repositories.gradle")

version = rootProject.version

dependencies {
    // generate own documentation with Orchid
    orchidRuntimeOnly(ModuleGroups.all)
}

// Orchid setup
//----------------------------------------------------------------------------------------------------------------------

orchid {
    githubToken = project.properties["githubToken"]?.toString() ?: ""
}

val build by tasks
val check by tasks
val orchidBuild by tasks
val orchidServe by tasks

build.dependsOn(orchidBuild)
orchidBuild.mustRunAfter(check)
orchidBuild.dependsOn(*ModuleGroups.all.tasksNamed("assemble"))
orchidServe.dependsOn(*ModuleGroups.all.tasksNamed("assemble"))
