package com.eden.orchid.wiki.utils

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiBookPage
import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSummaryPage
import org.jsoup.Jsoup

object WikiUtils {

// Wiki Creation Helpers
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Compile a summary file and traverse it to convert its links to wiki pages. For each link found, let a consumer
     * locate the appropriate OrchidResource for that link.
     */
    fun createWikiFromSummaryFile(
        section: WikiSection,
        summary: OrchidResource,
        onLinkDetected: (linkName: String, linkTarget: String, order: Int) -> OrchidResource
    ): Pair<WikiSummaryPage, List<WikiPage>> {
        val wiki = mutableListOf<WikiPage>()

        val content = summary.compileContent(null)
        val doc = Jsoup.parse(content)

        var previous: WikiPage? = null

        var i = 1

        for (a in doc.select("a[href]")) {
            if (OrchidUtils.isExternal(a.attr("href"))) continue

            val linkName = a.text()
            val linkTarget = a.attr("href")

            val resource = onLinkDetected(linkName, linkTarget, i)

            if (resource.reference.originalFileName.equals("index", ignoreCase = true)) {
                resource.reference.setAsDirectoryIndex()
            }

            val pageTitle = if (section.includeIndexInPageTitle) "${i + 1}. " + a.text() else a.text()

            val page = WikiPage(resource, pageTitle, section.key, i + 1)

            if(section.key.isBlank()) {
                page.reference.stripFromPath("wiki")
                page.reference.path = "wiki/${page.reference.originalPath}"
            }
            else {
                page.reference.stripFromPath("wiki/${section.key}")
                page.reference.path = "wiki/${section.key}/${page.reference.originalPath}"
            }

            i++

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

        val newSummary = StringResource(doc.toString(), summary.reference, summary.embeddedData)

        val definedSectionTitle = section.title
        val sectionTitle =
            if (!EdenUtils.isEmpty(definedSectionTitle)) definedSectionTitle
            else if (!EdenUtils.isEmpty(section.key)) section.key
            else "Wiki"

        val summaryPage = WikiSummaryPage(
            section.key,
            newSummary,
            sectionTitle from String::camelCase to Array<String>::titleCase
        )
        if(section.key.isBlank()) {
            summaryPage.reference.path = ""
            summaryPage.reference.fileName = "wiki"
            summaryPage.reference.isUsePrettyUrl = true
        }
        else {
            summaryPage.reference.path = "wiki"
            summaryPage.reference.fileName = section.key
            summaryPage.reference.isUsePrettyUrl = true
        }

        return summaryPage to wiki
    }

    fun createWikiFromResources(
        context: OrchidContext,
        section: WikiSection,
        resources: List<OrchidResource>
    ): Pair<WikiSummaryPage, List<WikiPage>> {
        val wikiPages = resources
            .sortedBy { it.reference.originalFullFileName }
            .mapIndexed { index, resource ->
                WikiPage(resource, "", section.key, index).also {
                    it.reference.path = "wiki/${section.key}"
                }
            }

        var summaryPageContent = "<ul>"
        for(page in wikiPages) {
            summaryPageContent += """<li><a href="${page.link}">${page.title}</a></li>"""
        }
        summaryPageContent += "</ul>"

        val definedSectionTitle = section.title
        val sectionTitle =
            if (!EdenUtils.isEmpty(definedSectionTitle)) definedSectionTitle
            else if (!EdenUtils.isEmpty(section.key)) section.key
            else "Wiki"

        val summaryPage = WikiSummaryPage(
            section.key,
            StringResource(context, "wiki/${section.key}.html", summaryPageContent),
            sectionTitle from String::camelCase to Array<String>::titleCase
        )

        return summaryPage to wikiPages
    }

// Generator Helpers
//----------------------------------------------------------------------------------------------------------------------

    fun linkWikiPages(summaryPage: WikiSummaryPage, wiki: List<WikiPage>) {
        // set up inter-page references properly
        for (wikiPage in wiki) {
            wikiPage.sectionSummary = summaryPage
            wikiPage.parent = summaryPage
        }
    }

    fun createWikiPdf(section: WikiSection) : WikiBookPage? {
        // create PDF from this section
        return if (section.createPdf) {
            val bookReference = OrchidReference(section.summaryPage.reference)

            if(section.key.isBlank()) {
                bookReference.path = "wiki"
                bookReference.fileName = "book"
                bookReference.extension = "pdf"
                bookReference.isUsePrettyUrl = false
            }
            else {
                bookReference.path = "wiki/${section.key}"
                bookReference.fileName = "book"
                bookReference.extension = "pdf"
                bookReference.isUsePrettyUrl = false
            }

            WikiBookPage(bookReference, section)
        } else {
            null
        }
    }
}
