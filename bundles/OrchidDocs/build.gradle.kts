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

    implementation(Modules.OrchidEditorial)

    implementation(Modules.OrchidPages)
    implementation(Modules.OrchidWiki)
    implementation(Modules.OrchidForms)
    implementation(Modules.OrchidChangelog)
    implementation(Modules.OrchidSearch)

    implementation(Modules.OrchidDiagrams)
    implementation(Modules.OrchidSyntaxHighlighter)
}
