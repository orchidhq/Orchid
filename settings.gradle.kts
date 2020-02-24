pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            } else if (requested.id.id == "kotlin-dce-js") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
        }
    }
}
buildscript {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("gradle.plugin.net.vivin:gradle-semantic-build-versioning:4.0.0")
    }
}
enableFeaturePreview("GRADLE_METADATA")

apply(plugin = "net.vivin.gradle-semantic-build-versioning")

rootProject.name = "Orchid"

include(
    ":OrchidCore",
    ":OrchidTest"
)

include(
    ":bundles:OrchidAll",
    ":bundles:OrchidBlog",
    ":bundles:OrchidDocs",
    ":bundles:OrchidLanguagePack"
)

include(
    ":integrations:OrchidAzure",
    ":integrations:OrchidBitbucket",
    ":integrations:OrchidGithub",
    ":integrations:OrchidGitlab",
    ":integrations:OrchidNetlify"
)

include(
    ":languageExtensions:OrchidAsciidoc",
    ":languageExtensions:OrchidBible",
    ":languageExtensions:OrchidDiagrams",
    ":languageExtensions:OrchidSyntaxHighlighter",
    ":languageExtensions:OrchidWritersBlocks"
)

include(
    ":plugins:OrchidChangelog",
    ":plugins:OrchidForms",
    ":plugins:OrchidGroovydoc",
    ":plugins:OrchidJavadoc",
    ":plugins:OrchidKotlindoc",
    ":plugins:OrchidKSS",
    ":plugins:OrchidNetlifyCMS",
    ":plugins:OrchidPages",
    ":plugins:OrchidPluginDocs",
    ":plugins:OrchidPosts",
    ":plugins:OrchidPresentations",
    ":plugins:OrchidSearch",
    ":plugins:OrchidSearch:widget",
    ":plugins:OrchidSourceDoc",
    ":plugins:OrchidSwagger",
    ":plugins:OrchidSwiftdoc",
    ":plugins:OrchidTaxonomies",
    ":plugins:OrchidWiki"
)

include(
    ":themes:OrchidBsDoc",
    ":themes:OrchidCopper",
    ":themes:OrchidEditorial",
    ":themes:OrchidFutureImperfect"
)
