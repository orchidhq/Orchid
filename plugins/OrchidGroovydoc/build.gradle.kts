apply(from = "$rootDir/gradle/groups/mainProjects.gradle")
apply(from = "$rootDir/gradle/groups/pluginProjects.gradle")

dependencies {
    implementation(Libs.com_eden_groovydoc_runner)
    implementation(Libs.com_eden_kodiak_groovydoc_runner)
    implementation(Modules.OrchidSourceDoc)
}
