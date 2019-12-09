buildscript {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("gradle.plugin.net.vivin:gradle-semantic-build-versioning:4.0.0")
    }
}
apply(plugin = "net.vivin.gradle-semantic-build-versioning")

rootProject.name = "Orchid"

includeModules(Projects.all)
includeModules(Projects.Plugins.all)
includeModules(Projects.LanguageExtensions.all)
includeModules(Projects.Integrations.all)
includeModules(Projects.Themes.all)
includeModules(Projects.Bundles.all)
