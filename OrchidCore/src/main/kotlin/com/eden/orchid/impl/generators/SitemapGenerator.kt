package com.eden.orchid.impl.generators

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.ArrayList
import java.util.stream.Stream
import javax.inject.Inject

@Description(value = "Generate a sitemap and `robots.txt` for automatic SEO.", name = "Sitemap and Robots.txt")
class SitemapGenerator @Inject
constructor(context: OrchidContext) : OrchidGenerator(context,
    GENERATOR_KEY, OrchidGenerator.PRIORITY_LATE + 1) {

    @Option
    @BooleanDefault(true)
    @Description("Whether to generate sitemaps.")
    var useSitemaps: Boolean = true

    @Option
    @BooleanDefault(true)
    @Description("Whether to generate a robots.txt, which includes a link to the sitemap.")
    var useRobots: Boolean = true

    override fun startIndexing(): List<OrchidPage> {
        return emptyList()
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        generateSitemapFiles()
    }

    private fun generateSitemapFiles() {
        var sitemapIndex: SitemapIndexPage? = null

        if (useSitemaps) {
            val mappedIndex = context.index.allIndexedPages
            val sitemapPages = ArrayList<SitemapPage>()

            // Render an page for each generator's individual index
            mappedIndex.keys.forEach { key ->
                val pages = mappedIndex[key]?.allPages
                val page = SitemapPage(context, key, pages)
                sitemapPages.add(page)
                context.renderRaw(page)
            }

            // Render an index of all indices, so individual index pages can be found
            sitemapIndex = SitemapIndexPage(context, sitemapPages)
            context.renderRaw(sitemapIndex)
        }

        if (useRobots) {
            // Render the robots.txt
            val robotsPage = RobotsPage(context, sitemapIndex)
            context.renderRaw(robotsPage)
        }
    }

    override fun getCollections(pages: List<OrchidPage>): List<OrchidCollection<*>> {
        return emptyList()
    }

    @Description(value = "The sitemap for a section of your site, grouped by generator.", name = "Sitemap")
    class SitemapPage internal constructor(context: OrchidContext, key: String, val entries: List<OrchidPage>?) :
        OrchidPage(context.getResourceEntry("sitemap.xml.peb"), "sitemap", "Sitemap") {
        init {
            this.reference.fileName = "sitemap-$key"
            this.reference.path = ""
            this.reference.outputExtension = "xml"
            this.reference.isUsePrettyUrl = false
        }
    }

    @Description(value = "The root sitemap, with links to all individual sitemaps.", name = "Sitemap Index")
    class SitemapIndexPage internal constructor(context: OrchidContext, val sitemaps: List<SitemapPage>) :
        OrchidPage(context.getResourceEntry("sitemapIndex.xml.peb"), "sitemapIndex", "Sitemap Index") {
        init {
            this.reference.fileName = "sitemap"
            this.reference.path = ""
            this.reference.outputExtension = "xml"
            this.reference.isUsePrettyUrl = false
        }
    }

    @Description(value = "Your site's robots.txt file, for directing web crawlers.", name = "Robots.txt")
    class RobotsPage internal constructor(context: OrchidContext, val sitemap: SitemapIndexPage?) :
        OrchidPage(context.getResourceEntry("robots.txt.peb"), "robots", "Robots") {
        init {
            this.reference.fileName = "robots"
            this.reference.path = ""
            this.reference.outputExtension = "txt"
            this.reference.isUsePrettyUrl = false
        }
    }

    companion object {
        val GENERATOR_KEY = "sitemap"
    }
}
