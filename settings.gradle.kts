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

Projects.includeAll(this)
Projects.Plugins.includeAll(this)
Projects.LanguageExtensions.includeAll(this)
Projects.Integrations.includeAll(this)
Projects.Themes.includeAll(this)
Projects.Bundles.includeAll(this)
