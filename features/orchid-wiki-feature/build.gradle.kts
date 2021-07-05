plugins {
    java
    kotlin("jvm")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
    `orchid-main-projects`
}

dependencies {
    implementation(Modules.OrchidCore)
    testImplementation(Modules.OrchidTest)

    // for PDF wiki generation
    implementation("com.openhtmltopdf:openhtmltopdf-core:1.0.8")
    implementation("com.openhtmltopdf:openhtmltopdf-pdfbox:1.0.8")
    implementation("com.openhtmltopdf:openhtmltopdf-slf4j:1.0.8")
    implementation("com.openhtmltopdf:openhtmltopdf-svg-support:1.0.8")
}
