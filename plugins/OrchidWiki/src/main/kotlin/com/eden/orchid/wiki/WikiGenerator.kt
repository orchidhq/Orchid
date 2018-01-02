package com.eden.orchid.wiki

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.wiki.model.WikiModel
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSummaryPage
import org.apache.commons.io.FilenameUtils
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.File
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Create a structured and navigable knowledge-base for your project.")
class WikiGenerator @Inject
constructor(context: OrchidContext, private val model: WikiModel) : OrchidGenerator(context, "wiki", 700), OptionsHolder {

    @Option("baseDir") @StringDefault("wiki")
    lateinit var wikiBaseDir: String

    @Option("sections")
    lateinit var sectionNames: Array<String>

    override fun startIndexing(): List<OrchidPage> {
        model.initialize()

        if (EdenUtils.isEmpty(sectionNames)) {
            val wiki = getWikiPages(null)
            if (wiki != null) {
                model.sections.put(null, wiki)
            }
        } else {
            for (section in sectionNames) {
                val wiki = getWikiPages(section)
                if (wiki != null) {
                    model.sections.put(section, wiki)
                }
            }
        }

        return model.allPages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

    private fun getWikiPages(section: String?): WikiSection? {
        val pageMenuItem = JSONObject()
        pageMenuItem.put("type", "wiki")

        if (!EdenUtils.isEmpty(section)) {
            pageMenuItem.put("title", section!! + " Wiki")
            pageMenuItem.put("section", section)
        } else {
            pageMenuItem.put("title", "Wiki")
        }

        val wiki = ArrayList<WikiPage>()

        val sectionBaseDir = if (!EdenUtils.isEmpty(section))
            OrchidUtils.normalizePath(wikiBaseDir) + "/" + OrchidUtils.normalizePath(section) + "/"
        else
            OrchidUtils.normalizePath(wikiBaseDir) + "/"

        var summary: OrchidResource? = context.getLocalResourceEntry(sectionBaseDir + "SUMMARY.md")

        if (summary == null) {
            if(section != null) {
                Clog.w("Could not find wiki summary page in '#{}'", sectionBaseDir)
            }

            return null
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

            val page = WikiPage(resource, a.text(), i)

            page.menu.addMenuItem(pageMenuItem)

            i++
            page.order = i

            wiki.add(page)

            if (previous != null) {
                previous.next = page
                page.previous = previous

                previous = page
            } else {
                previous = page
            }

            a.attr("href", page.reference.toString())
        }

        val safe = doc.toString()
        summary = StringResource(context, sectionBaseDir + "summary.md", safe)

        val sectionTitle = if (!EdenUtils.isEmpty(section)) section else "Wiki"
        val summaryPage = WikiSummaryPage(summary, OrchidUtils.camelcaseToTitleCase(sectionTitle))
        summaryPage.reference.isUsePrettyUrl = true
        summaryPage.menu.addMenuItem(pageMenuItem)

        for (wikiPage in wiki) {
            wikiPage.sectionSummary = summaryPage
        }

        return WikiSection(section, summaryPage, wiki)
    }

}

