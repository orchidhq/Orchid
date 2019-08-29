dependencies {
    val ver = "0.3.2"
    implementation("com.eden.kodiak:common-models:$ver")
    implementation("com.eden.kodiak:common-formatter:$ver")
    implementation("com.eden.kodiak:common-runner:$ver")

    testImplementation("com.eden.kodiak:dokka-runner:$ver")
    testImplementation("com.eden.kodiak:javadoc-runner:$ver")
    testImplementation("com.eden.kodiak:groovydoc-runner:$ver")
}
