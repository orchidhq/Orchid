
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

enum class Module(val path: String) {
    OrchidCore(":OrchidCore"),
    OrchidTest(":OrchidTest"),
    OrchidAsciidoc(":languageExtensions:OrchidAsciidoc"),
    OrchidBible(":languageExtensions:OrchidBible"),
    OrchidDiagrams(":languageExtensions:OrchidDiagrams"),
    OrchidSyntaxHighlighter(":languageExtensions:OrchidSyntaxHighlighter"),
    OrchidWritersBlocks(":languageExtensions:OrchidWritersBlocks"),
    OrchidAzure(":integrations:OrchidAzure"),
    OrchidBitbucket(":integrations:OrchidBitbucket"),
    OrchidGithub(":integrations:OrchidGithub"),
    OrchidGitlab(":integrations:OrchidGitlab"),
    OrchidNetlify(":integrations:OrchidNetlify"),
    OrchidChangelog(":plugins:OrchidChangelog"),
    OrchidForms(":plugins:OrchidForms"),
    OrchidGroovydoc(":plugins:OrchidGroovydoc"),
    OrchidJavadoc(":plugins:OrchidJavadoc"),
    OrchidKotlindoc(":plugins:OrchidKotlindoc"),
    OrchidKSS(":plugins:OrchidKSS"),
    OrchidNetlifyCMS(":plugins:OrchidNetlifyCMS"),
    OrchidPages(":plugins:OrchidPages"),
    OrchidPluginDocs(":plugins:OrchidPluginDocs"),
    OrchidPosts(":plugins:OrchidPosts"),
    OrchidPresentations(":plugins:OrchidPresentations"),
    OrchidSearch(":plugins:OrchidSearch"),
    OrchidSourceDoc(":plugins:OrchidSourceDoc"),
    OrchidSwagger(":plugins:OrchidSwagger"),
    OrchidSwiftdoc(":plugins:OrchidSwiftdoc"),
    OrchidTaxonomies(":plugins:OrchidTaxonomies"),
    OrchidWiki(":plugins:OrchidWiki"),
    OrchidAll(":bundles:OrchidAll"),
    OrchidBlog(":bundles:OrchidBlog"),
    OrchidDocs(":bundles:OrchidDocs"),
    OrchidLanguagePack(":bundles:OrchidLanguagePack"),
    OrchidBsDoc(":themes:OrchidBsDoc"),
    OrchidCopper(":themes:OrchidCopper"),
    OrchidEditorial(":themes:OrchidEditorial"),
    OrchidFutureImperfect(":themes:OrchidFutureImperfect");
}

object Projects {

    val all: List<Module> = listOf(
        Module.OrchidCore,
        Module.OrchidTest
    )
    
    object LanguageExtensions {
        val all: List<Module> = listOf(
            Module.OrchidAsciidoc,
            Module.OrchidBible,
            Module.OrchidDiagrams,
            Module.OrchidSyntaxHighlighter,
            Module.OrchidWritersBlocks
        )
    }

    object Integrations {
        val all: List<Module> = listOf(
            Module.OrchidAzure,
            Module.OrchidBitbucket,
            Module.OrchidGithub,
            Module.OrchidGitlab,
            Module.OrchidNetlify
        )
    }

    object Plugins {
        val all: List<Module> = listOf(
            Module.OrchidChangelog,
            Module.OrchidForms,
            Module.OrchidGroovydoc,
            Module.OrchidJavadoc,
            Module.OrchidKotlindoc,
            Module.OrchidKSS,
            Module.OrchidNetlifyCMS,
            Module.OrchidPages,
            Module.OrchidPluginDocs,
            Module.OrchidPosts,
            Module.OrchidPresentations,
            Module.OrchidSearch,
            Module.OrchidSourceDoc,
            Module.OrchidSwagger,
            Module.OrchidSwiftdoc,
            Module.OrchidTaxonomies,
            Module.OrchidWiki,
            Module.OrchidGitlab,
            Module.OrchidNetlify
        )
    }

    object Themes {
        val all: List<Module> = listOf(
            Module.OrchidBsDoc,
            Module.OrchidCopper,
            Module.OrchidEditorial,
            Module.OrchidFutureImperfect
        )
    }

    object Bundles {
        val all: List<Module> = listOf(
            Module.OrchidAll,
            Module.OrchidBlog,
            Module.OrchidDocs,
            Module.OrchidLanguagePack
        )
    }
}

fun DependencyHandlerScope.compile(vararg modules: Module)
        = dependsOnModules(*modules, configurationName = "compile")
fun DependencyHandlerScope.implementation(vararg modules: Module)
        = dependsOnModules(*modules, configurationName = "implementation")
fun DependencyHandlerScope.testImplementation(vararg modules: Module)
        = dependsOnModules(*modules, configurationName = "testImplementation")
fun DependencyHandlerScope.dependsOnModules(vararg modules: Module, configurationName: String = "compile") {
    dependsOnModules(modules.toList(), configurationName)
}

fun DependencyHandlerScope.compile(modules: Iterable<Module>)
        = dependsOnModules(modules, "compile")
fun DependencyHandlerScope.implementation(modules: Iterable<Module>)
        = dependsOnModules(modules, "implementation")
fun DependencyHandlerScope.testImplementation(modules: Iterable<Module>)
        = dependsOnModules(modules, "testImplementation")
fun DependencyHandlerScope.dependsOnModules(modules: Iterable<Module>, configurationName: String = "compile") {
    modules.forEach { add(configurationName, project(it.path)) }
}

fun Settings.includeModules(modules: Iterable<Module>) {
    modules.forEach { this.include(it.path) }
}
