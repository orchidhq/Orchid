apply(from = "$rootDir/gradle/groups/mainProjects.gradle")
apply(from = "$rootDir/gradle/groups/pluginProjects.gradle")

dependencies {
    implementation(Libs.com_eden_dokka_runner)
    implementation(Libs.com_eden_kodiak_dokka_runner)
    implementation(Modules.OrchidSourceDoc)
}
