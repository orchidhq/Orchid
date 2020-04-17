apply(from = "$rootDir/gradle/groups/mainProjects.gradle")
apply(from = "$rootDir/gradle/groups/pluginProjects.gradle")

dependencies {
    implementation(Libs.swiftdoc_runner)
    implementation(Modules.OrchidSourceDoc)
}
