package com.eden.orchid.wiki

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ImpliedKey
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import com.eden.orchid.wiki.model.WikiModel
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSectionsPage
import com.eden.orchid.wiki.pages.WikiSummaryPage
import org.apache.commons.io.FilenameUtils
import org.jsoup.Jsoup
import java.io.File
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Description("Create a structured and navigable knowledge-base for your project.", name = "Wiki")
class WikiGenerator
@Inject
constructor(
        context: OrchidContext,
        private val wikiModel: WikiModel
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "wiki"
    }

    @Option
    @StringDefault("wiki")
    @Description("The base directory in local resources to look for wikis in.")
    lateinit var baseDir: String

    @Option
    @ImpliedKey("key")
    @Description("The sections within the baseDir to make wikis out of.")
    lateinit var sections: MutableList<WikiSection>

    @Option
    @Description("The configuration for the default wiki, when no other categories are set up.")
    lateinit var defaultConfig: WikiSection

    override fun startIndexing(): List<OrchidPage> {
        if (EdenUtils.isEmpty(sections)) {
            sections.add(defaultConfig)
        }

        wikiModel.initialize(sections)

        wikiModel.sections.values.forEach { section ->
            loadWikiPages(section)
        }

        if (sections.size > 1) {
            wikiModel.sectionsPage = getSectionsIndex()
        }

        return wikiModel.allPages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

    private fun loadWikiPages(section: WikiSection) {
        val wiki = ArrayList<WikiPage>()

        val sectionBaseDir = if (!EdenUtils.isEmpty(section.key))
            OrchidUtils.normalizePath(baseDir) + "/" + OrchidUtils.normalizePath(section.key) + "/"
        else
            OrchidUtils.normalizePath(baseDir) + "/"

        val summary: OrchidResource? = context.locateLocalResourceEntry(sectionBaseDir + "summary")

        if (summary == null) {
            if (section.key != null) {
                Clog.w("Could not find wiki summary page in '#{}'", sectionBaseDir)
            }

            return
        }

        val content = context.compile(summary.reference.extension, summary.content)
        val doc = Jsoup.parse(content)

        val links = doc.select("a[href]")

        var previous: WikiPage? = null

        var i = 0

        for (a in links) {
            val file = sectionBaseDir + a.attr("href")
            val path = sectionBaseDir + FilenameUtils.removeExtension(a.attr("href"))

            var resource: OrchidResource? = context.getLocalResourceEntry(file)

            if (resource == null) {
                Clog.w("Could not find wiki resource page at '#{$1}'", file)
                resource = StringResource(context, path + File.separator + "index.md", a.text())
            }

            val pageTitle = if (section.includeIndexInPageTitle) "${i + 1}. " + a.text() else a.text()

            val page = WikiPage(resource, pageTitle, section.key, i + 1)

            i++

            wiki.add(page)

            if (previous != null) {
                previous.next = page
                page.previous = previous

                previous = page
            }
            else {
                previous = page
            }

            a.attr("href", page.reference.toString())
        }

        val definedSectionTitle = summary.queryEmbeddedData("title")?.element as? String? ?: ""

        val safe = doc.toString()
        val summaryReference = OrchidReference(summary.reference)

        val segments = summaryReference.originalPath.split("/")
        summaryReference.fileName = segments.last()
        summaryReference.path = segments.subList(0, segments.size - 1).joinToString("/")
        val newSummary = StringResource(safe, summaryReference, summary.embeddedData)

        val sectionTitle =
                if (!EdenUtils.isEmpty(definedSectionTitle)) definedSectionTitle
                else if (!EdenUtils.isEmpty(section.key)) section.key!!
                else "Wiki"

        val summaryPage = WikiSummaryPage(section.key, newSummary, sectionTitle from String::camelCase to Array<String>::titleCase)
        summaryPage.reference.isUsePrettyUrl = true

        for (wikiPage in wiki) {
            wikiPage.sectionSummary = summaryPage
            wikiPage.parent = summaryPage
        }

        section.summaryPage = summaryPage
        section.wikiPages = wiki
    }

    private fun getSectionsIndex(): WikiSectionsPage {
        val resource = StringResource(context, OrchidUtils.normalizePath(baseDir) + ".md", "")

        val sectionsPage = WikiSectionsPage(wikiModel, resource, "Wiki")

        for (summaryPage in wikiModel.sections.values) {
            summaryPage.summaryPage.sectionsPage = sectionsPage
            summaryPage.summaryPage.parent = sectionsPage
        }

        return sectionsPage
    }

    override fun getCollections(): List<OrchidCollection<*>> {
        val collectionsList = java.util.ArrayList<OrchidCollection<*>>()

        wikiModel.sections.forEach {
            var sectionPages = ArrayList<OrchidPage>()

            sectionPages.add(it.value.summaryPage)
            sectionPages.addAll(it.value.wikiPages)

            val collection = FileCollection(this, it.key, sectionPages)
            collectionsList.add(collection)
        }

        return collectionsList
    }

}

