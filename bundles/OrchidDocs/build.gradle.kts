apply(from = "$rootDir/gradle/groups/bundleProjects.gradle")

dependencies {
    implementation(Modules.OrchidCore)

    implementation(Modules.OrchidEditorial)

    implementation(Modules.OrchidPages)
    implementation(Modules.OrchidWiki)
    implementation(Modules.OrchidForms)
    implementation(Modules.OrchidChangelog)
    implementation(Modules.OrchidSearch)

    implementation(Modules.OrchidDiagrams)
    implementation(Modules.OrchidSyntaxHighlighter)
}
