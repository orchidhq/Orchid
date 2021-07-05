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

    implementation(Modules.OrchidPosts)
    implementation(Modules.OrchidPages)
    implementation(Modules.OrchidForms)
    implementation(Modules.OrchidArchives)

    implementation(*ModuleGroups.LanguageExtensions.all)
}
