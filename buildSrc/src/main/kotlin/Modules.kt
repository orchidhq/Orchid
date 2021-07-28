
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project



class Modules(val dependencyHandler: DependencyHandler) {
    val core get() = dependencyHandler.project(":orchid-core")
    val test get() = dependencyHandler.project(":orchid-test")

    inner class Features {
        val archives          get() = dependencyHandler.project(":features:orchid-archives-feature")
        val asciidoc          get() = dependencyHandler.project(":features:orchid-asciidoc-feature")
        val azure             get() = dependencyHandler.project(":features:orchid-azure-feature")
        val bible             get() = dependencyHandler.project(":features:orchid-bible-feature")
        val bitbucket         get() = dependencyHandler.project(":features:orchid-bitbucket-feature")
        val changelog         get() = dependencyHandler.project(":features:orchid-changelog-feature")
        val diagrams          get() = dependencyHandler.project(":features:orchid-diagrams-feature")
        val forms             get() = dependencyHandler.project(":features:orchid-forms-feature")
        val github            get() = dependencyHandler.project(":features:orchid-github-feature")
        val gitlab            get() = dependencyHandler.project(":features:orchid-gitlab-feature")
        val groovydoc         get() = dependencyHandler.project(":features:orchid-groovydoc-feature")
        val javadoc           get() = dependencyHandler.project(":features:orchid-javadoc-feature")
        val kotlindoc         get() = dependencyHandler.project(":features:orchid-kotlindoc-feature")
        val kss               get() = dependencyHandler.project(":features:orchid-kss-feature")
        val netlify           get() = dependencyHandler.project(":features:orchid-netlify-feature")
        val netlifyCMS        get() = dependencyHandler.project(":features:orchid-netlify-cms-feature")
        val pages             get() = dependencyHandler.project(":features:orchid-pages-feature")
        val pluginDocs        get() = dependencyHandler.project(":features:orchid-plugin-docs-feature")
        val posts             get() = dependencyHandler.project(":features:orchid-posts-feature")
        val presentations     get() = dependencyHandler.project(":features:orchid-presentations-feature")
        val search            get() = dependencyHandler.project(":features:orchid-search-feature")
        val snippets          get() = dependencyHandler.project(":features:orchid-snippets-feature")
        val sourceDoc         get() = dependencyHandler.project(":features:orchid-sourcedoc-feature")
        val swagger           get() = dependencyHandler.project(":features:orchid-swagger-feature")
        val swiftdoc          get() = dependencyHandler.project(":features:orchid-swiftdoc-feature")
        val syntaxHighlighter get() = dependencyHandler.project(":features:orchid-syntax-highlighter-feature")
        val wiki              get() = dependencyHandler.project(":features:orchid-wiki-feature")
        val writersBlocks     get() = dependencyHandler.project(":features:orchid-writers-blocks-feature")
    }

    inner class Themes {
        val bsdoc           get() = dependencyHandler.project(":themes:orchid-bsdoc-theme")
        val copper          get() = dependencyHandler.project(":themes:orchid-copper-theme")
        val editorial       get() = dependencyHandler.project(":themes:orchid-editorial-theme")
        val futureImperfect get() = dependencyHandler.project(":themes:orchid-future-imperfect-theme")
    }

    inner class Bundles {
        val languages get() = arrayOf(
            features.asciidoc,
            features.bible,
            features.diagrams,
            features.syntaxHighlighter,
            features.writersBlocks
        )
    }

    val features get() = Features()
    val themes get() = Themes()
    val bundles get() = Bundles()
}

val DependencyHandler.moduleLibs get() = Modules(this)
