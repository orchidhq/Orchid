
object ModuleGroups {
    val topLevelModules: Array<Module> = arrayOf(
        Modules.OrchidCore,
        Modules.OrchidTest
    )

    val all: Array<Module> by lazy {
        ModuleGroups.topLevelModules + LanguageExtensions.all + Integrations.all + Plugins.all + Themes.all
    }

    object LanguageExtensions {
        val all: Array<Module> = arrayOf(
            Modules.OrchidAsciidoc,
            Modules.OrchidBible,
            Modules.OrchidDiagrams,
            Modules.OrchidSyntaxHighlighter,
            Modules.OrchidWritersBlocks
        )
    }

    object Integrations {
        val all: Array<Module> = arrayOf(
            Modules.OrchidAzure,
            Modules.OrchidBitbucket,
            Modules.OrchidGithub,
            Modules.OrchidGitlab,
            Modules.OrchidNetlify
        )
    }

    object Plugins {
        val all: Array<Module> = arrayOf(
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
        val all: Array<Module> = arrayOf(
            Modules.OrchidBsDoc,
            Modules.OrchidCopper,
            Modules.OrchidEditorial,
            Modules.OrchidFutureImperfect
        )
    }

    object Bundles {
        val all: Array<Module> = arrayOf(
            Modules.OrchidAll,
            Modules.OrchidBlog,
            Modules.OrchidDocs,
            Modules.OrchidLanguagePack
        )
    }
}
