apply(from = "$rootDir/gradle/groups/mainProjects.gradle")
apply(from = "$rootDir/gradle/groups/pluginProjects.gradle")

dependencies {
    implementation(Libs.jsoup)

    // for PDF wiki generation
    implementation(Libs.openhtmltopdf_core)
    implementation(Libs.openhtmltopdf_pdfbox)
    implementation(Libs.openhtmltopdf_slf4j)
    implementation(Libs.openhtmltopdf_svg_support)
}
