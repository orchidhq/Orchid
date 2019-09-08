dependencies {
    implementation(Libs.common_models)
    implementation(Libs.common_formatter)
    implementation(Libs.common_runner)

    testImplementation(Module.OrchidJavadoc)
    testImplementation(Module.OrchidGroovydoc)
    testImplementation(Module.OrchidKotlindoc)
    testImplementation(Module.OrchidSwiftdoc)
}
