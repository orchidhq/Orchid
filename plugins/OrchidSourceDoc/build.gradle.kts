dependencies {
    implementation(Libs.common_models)
    implementation(Libs.common_formatter)
    implementation(Libs.common_runner)

    testImplementation(Projects.Plugins.OrchidJavadoc(this))
    testImplementation(Projects.Plugins.OrchidGroovydoc(this))
    testImplementation(Projects.Plugins.OrchidKotlindoc(this))
    testImplementation(Projects.Plugins.OrchidSwiftdoc(this))
}
