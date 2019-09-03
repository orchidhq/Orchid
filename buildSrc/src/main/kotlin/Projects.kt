import Projects.Integrations.OrchidGitlab
import Projects.Integrations.OrchidNetlify
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.project

object Projects {

    fun OrchidCore(dependencyHandler: DependencyHandler) =
        dependencyHandler.project(":OrchidCore")

    fun OrchidTest(dependencyHandler: DependencyHandler) =
        dependencyHandler.project(":OrchidTest")

    object LanguageExtensions {
        fun OrchidAsciidoc(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":languageExtensions:OrchidAsciidoc")

        fun OrchidBible(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":languageExtensions:OrchidBible")

        fun OrchidDiagrams(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":languageExtensions:OrchidDiagrams")

        fun OrchidSyntaxHighlighter(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":languageExtensions:OrchidSyntaxHighlighter")

        fun OrchidWritersBlocks(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":languageExtensions:OrchidWritersBlocks")

        fun all(dependencyHandler: DependencyHandler) = listOf(
            OrchidAsciidoc(dependencyHandler),
            OrchidBible(dependencyHandler),
            OrchidDiagrams(dependencyHandler),
            OrchidSyntaxHighlighter(dependencyHandler),
            OrchidWritersBlocks(dependencyHandler)
        )

        fun includeAll(settings: Settings) = with(settings) {
            include(":languageExtensions:OrchidAsciidoc")
            include(":languageExtensions:OrchidBible")
            include(":languageExtensions:OrchidDiagrams")
            include(":languageExtensions:OrchidSyntaxHighlighter")
            include(":languageExtensions:OrchidWritersBlocks")
        }
    }

    object Integrations {
        fun OrchidAzure(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":integrations:OrchidAzure")

        fun OrchidBitbucket(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":integrations:OrchidBitbucket")

        fun OrchidGithub(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":integrations:OrchidGithub")

        fun OrchidGitlab(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":integrations:OrchidGitlab")

        fun OrchidNetlify(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":integrations:OrchidNetlify")

        fun all(dependencyHandler: DependencyHandler) = listOf(
            OrchidAzure(dependencyHandler),
            OrchidBitbucket(dependencyHandler),
            OrchidGithub(dependencyHandler),
            OrchidGitlab(dependencyHandler),
            OrchidNetlify(dependencyHandler)
        )

        fun includeAll(settings: Settings) = with(settings) {
            include(":integrations:OrchidAzure")
            include(":integrations:OrchidBitbucket")
            include(":integrations:OrchidGithub")
            include(":integrations:OrchidGitlab")
            include(":integrations:OrchidNetlify")
        }
    }

    object Plugins {

        fun OrchidChangelog(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidChangelog")

        fun OrchidForms(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidForms")

        fun OrchidGroovydoc(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidGroovydoc")

        fun OrchidJavadoc(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidJavadoc")

        fun OrchidKotlindoc(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidKotlindoc")

        fun OrchidKSS(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidKSS")

        fun OrchidNetlifyCMS(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidNetlifyCMS")

        fun OrchidPages(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidPages")

        fun OrchidPluginDocs(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidPluginDocs")

        fun OrchidPosts(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidPosts")

        fun OrchidPresentations(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidPresentations")

        fun OrchidSearch(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidSearch")

        fun OrchidSourceDoc(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidSourceDoc")

        fun OrchidSwagger(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidSwagger")

        fun OrchidSwiftdoc(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidSwiftdoc")

        fun OrchidTaxonomies(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidTaxonomies")

        fun OrchidWiki(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":plugins:OrchidWiki")

        fun all(dependencyHandler: DependencyHandler) = listOf(
            OrchidChangelog(dependencyHandler),
            OrchidForms(dependencyHandler),
            OrchidGroovydoc(dependencyHandler),
            OrchidJavadoc(dependencyHandler),
            OrchidKotlindoc(dependencyHandler),
            OrchidKSS(dependencyHandler),
            OrchidNetlifyCMS(dependencyHandler),
            OrchidPages(dependencyHandler),
            OrchidPluginDocs(dependencyHandler),
            OrchidPosts(dependencyHandler),
            OrchidPresentations(dependencyHandler),
            OrchidSearch(dependencyHandler),
            OrchidSourceDoc(dependencyHandler),
            OrchidSwagger(dependencyHandler),
            OrchidSwiftdoc(dependencyHandler),
            OrchidTaxonomies(dependencyHandler),
            OrchidWiki(dependencyHandler),
            OrchidGitlab(dependencyHandler),
            OrchidNetlify(dependencyHandler)
        )

        fun includeAll(settings: Settings) = with(settings) {
            include(":plugins:OrchidChangelog")
            include(":plugins:OrchidForms")
            include(":plugins:OrchidGroovydoc")
            include(":plugins:OrchidJavadoc")
            include(":plugins:OrchidKotlindoc")
            include(":plugins:OrchidKSS")
            include(":plugins:OrchidNetlifyCMS")
            include(":plugins:OrchidPages")
            include(":plugins:OrchidPluginDocs")
            include(":plugins:OrchidPosts")
            include(":plugins:OrchidPresentations")
            include(":plugins:OrchidSearch")
            include(":plugins:OrchidSourceDoc")
            include(":plugins:OrchidSwagger")
            include(":plugins:OrchidSwiftdoc")
            include(":plugins:OrchidTaxonomies")
            include(":plugins:OrchidWiki")
        }
    }

    object Themes {
        fun OrchidBsDoc(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":themes:OrchidBsDoc")

        fun OrchidCopper(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":themes:OrchidCopper")

        fun OrchidEditorial(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":themes:OrchidEditorial")

        fun OrchidFutureImperfect(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":themes:OrchidFutureImperfect")

        fun all(dependencyHandler: DependencyHandler) = listOf(
            OrchidBsDoc(dependencyHandler),
            OrchidCopper(dependencyHandler),
            OrchidEditorial(dependencyHandler),
            OrchidFutureImperfect(dependencyHandler)
        )

        fun includeAll(settings: Settings) = with(settings) {
            include(":themes:OrchidBsDoc")
            include(":themes:OrchidCopper")
            include(":themes:OrchidEditorial")
            include(":themes:OrchidFutureImperfect")
        }
    }

    object Bundles {
        fun OrchidAll(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":themes:OrchidAll")

        fun OrchidBlog(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":themes:OrchidBlog")

        fun OrchidDocs(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":themes:OrchidDocs")

        fun OrchidLanguagePack(dependencyHandler: DependencyHandler) =
            dependencyHandler.project(":themes:OrchidLanguagePack")

        fun all(dependencyHandler: DependencyHandler) = listOf(
            OrchidAll(dependencyHandler),
            OrchidBlog(dependencyHandler),
            OrchidDocs(dependencyHandler),
            OrchidLanguagePack(dependencyHandler)
        )

        fun includeAll(settings: Settings) = with(settings) {
            include(":bundles:OrchidAll")
            include(":bundles:OrchidBlog")
            include(":bundles:OrchidDocs")
            include(":bundles:OrchidLanguagePack")
        }
    }

    fun all(dependencyHandler: DependencyHandler) = listOf(
        OrchidCore(dependencyHandler),
        OrchidTest(dependencyHandler)
    )

    fun includeAll(settings: Settings) = with(settings) {
        include(":OrchidCore")
        include(":OrchidTest")
    }
}
