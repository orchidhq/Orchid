package com.eden.orchid.wiki.pages

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.convertOutputStream
import com.eden.orchid.wiki.model.WikiSection
import com.openhtmltopdf.DOMBuilder
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import com.openhtmltopdf.svgsupport.BatikSVGDrawer
import org.jsoup.Jsoup
import java.io.InputStream
import java.util.regex.Pattern

class WikiBookResource(
        reference: OrchidReference,
        val section: WikiSection
) : OrchidResource(reference) {

    init {
        rawContent = ""
        content = ""
        embeddedData = null
    }

    override fun getContentStream(): InputStream? {
        return convertOutputStream { safeOs ->
            var pdfOutput = section.summaryPage.content.replaceBaseUrls()

            for (wikiPage in section.wikiPages) {
                pdfOutput += "<h1 id=\"${wikiPage.link.formatAnchor()}\" style=\"page-break-before: always;\">${wikiPage.title}</h1>"
                pdfOutput += wikiPage.content.replaceBaseUrls()
            }

            val doc = Jsoup.parse(pdfOutput)
            val pdfDoc = DOMBuilder.jsoup2DOM(doc)

            PdfRendererBuilder()
                    .useSVGDrawer(BatikSVGDrawer())
                    .withW3cDocument(pdfDoc, context.baseUrl)
                    .toStream(safeOs)
                    .run()
        }
    }

    fun String.replaceBaseUrls(): String {
        val pattern = "href=\"(${Pattern.quote(context.baseUrl)}(.*?))\"".toRegex()

        return pattern.replace(this) { match ->
            val formattedId = match.groupValues[2].replace("/", "_")
            "href=\"#$formattedId\""
        }
    }

    fun String.formatAnchor(): String {
        return this.replace(context.baseUrl, "").replace("/", "_")
    }
}
