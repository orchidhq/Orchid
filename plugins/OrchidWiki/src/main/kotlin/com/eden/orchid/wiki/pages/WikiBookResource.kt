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
            val wikiBookTemplate = context.locateTemplate("wiki/book")
            val pdfOutput = wikiBookTemplate.compileContent(mapOf(
                    "section" to section,
                    "resource" to this@WikiBookResource
            ))

            val doc = Jsoup.parse(pdfOutput)
            val pdfDoc = DOMBuilder.jsoup2DOM(doc)

            PdfRendererBuilder()
                    .useSVGDrawer(BatikSVGDrawer())
                    .withW3cDocument(pdfDoc, context.baseUrl)
                    .toStream(safeOs)
                    .run()
        }
    }

    fun replaceBaseUrls(input: String): String {
        val pattern = "href=\"(${Pattern.quote(context.baseUrl)}(.*?))\"".toRegex()

        return pattern.replace(input) { match ->
            val formattedId = match.groupValues[2].replace("/", "_")
            "href=\"#$formattedId\""
        }
    }

    fun formatAnchor(input: String): String {
        return input.replace(context.baseUrl, "").replace("/", "_")
    }
}
