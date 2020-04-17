apply(from = "$rootDir/gradle/groups/mainProjects.gradle")
apply(from = "$rootDir/gradle/groups/pluginProjects.gradle")

dependencies {
    implementation(Modules.OrchidChangelog)
    implementation(Modules.OrchidWiki)
}
