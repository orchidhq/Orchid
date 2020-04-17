apply(from = "$rootDir/gradle/groups/bundleProjects.gradle")

dependencies {
    implementation(Modules.OrchidCore)

    implementation(Modules.OrchidPosts)
    implementation(Modules.OrchidPages)
    implementation(Modules.OrchidForms)
    implementation(Modules.OrchidTaxonomies)

    implementation(ModuleGroups.LanguageExtensions.all)
}
