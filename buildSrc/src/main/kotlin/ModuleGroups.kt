
object ModuleGroups {
    val topLevelModules: List<Module> = listOf(
        Modules.OrchidCore,
        Modules.OrchidTest
    )

    val all: List<Module> by lazy {
        ModuleGroups.topLevelModules + LanguageExtensions.all + Integrations.all + Plugins.all + Themes.all
    }

    object LanguageExtensions {
        val all: List<Module> = listOf(
            Modules.OrchidAsciidoc,
            Modules.OrchidBible,
            Modules.OrchidDiagrams,
            Modules.OrchidSyntaxHighlighter,
            Modules.OrchidWritersBlocks
        )
    }

    object Integrations {
        val all: List<Module> = listOf(
            Modules.OrchidAzure,
            Modules.OrchidBitbucket,
            Modules.OrchidGithub,
            Modules.OrchidGitlab,
            Modules.OrchidNetlify
        )
    }

    object Plugins {
        val all: List<Module> = listOf(
            Modules.OrchidChangelog,
            Modules.OrchidForms,
            Modules.OrchidGroovydoc,
            Modules.OrchidJavadoc,
            Modules.OrchidKotlindoc,
            Modules.OrchidKSS,
            Modules.OrchidNetlifyCMS,
            Modules.OrchidPages,
            Modules.OrchidPluginDocs,
            Modules.OrchidPosts,
            Modules.OrchidPresentations,
            Modules.OrchidSearch,
            Modules.OrchidSnippets,
            Modules.OrchidSourceDoc,
            Modules.OrchidSwagger,
            Modules.OrchidSwiftdoc,
            Modules.OrchidTaxonomies,
            Modules.OrchidWiki
        )
    }

    object Themes {
        val all: List<Module> = listOf(
            Modules.OrchidBsDoc,
            Modules.OrchidCopper,
            Modules.OrchidEditorial,
            Modules.OrchidFutureImperfect
        )
    }

    object Bundles {
        val all: List<Module> = listOf(
            Modules.OrchidAll,
            Modules.OrchidBlog,
            Modules.OrchidDocs,
            Modules.OrchidLanguagePack
        )
    }
}
