apply(from = "$rootDir/gradle/groups/mainProjects.gradle")
apply(from = "$rootDir/gradle/groups/pluginProjects.gradle")

dependencies {
    implementation(Libs.common_models)
    implementation(Libs.common_formatter)
    implementation(Libs.common_runner)

    testImplementation(Modules.OrchidJavadoc)
    testImplementation(Modules.OrchidGroovydoc)
    testImplementation(Modules.OrchidKotlindoc)
    testImplementation(Modules.OrchidSwiftdoc)
    testImplementation(Modules.OrchidPluginDocs)
}
