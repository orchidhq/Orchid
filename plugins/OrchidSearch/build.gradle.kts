apply(from = "$rootDir/gradle/groups/mainProjects.gradle")
apply(from = "$rootDir/gradle/groups/pluginProjects.gradle")

dependencies {
    testImplementation(Modules.OrchidSourceDoc)
    testImplementation(Modules.OrchidKotlindoc)
    testImplementation(Modules.OrchidPosts)
    testImplementation(Modules.OrchidPages)
    testImplementation(Modules.OrchidWiki)
}
