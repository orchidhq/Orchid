apply(from = "$rootDir/gradle/groups/bundleProjects.gradle")

dependencies {
    implementation(Modules.OrchidCore)
    implementation(ModuleGroups.Plugins.all)
    implementation(ModuleGroups.Themes.all)
    implementation(ModuleGroups.LanguageExtensions.all)
    implementation(ModuleGroups.Integrations.all)
}
