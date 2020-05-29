package com.eden.orchid.wiki.pages

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.convertOutputStream
import com.eden.orchid.wiki.model.WikiSection
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import com.openhtmltopdf.svgsupport.BatikSVGDrawer
import org.jsoup.Jsoup
import org.jsoup.helper.W3CDom
import java.io.InputStream
import java.util.regex.Pattern

class WikiBookResource(
        reference: OrchidReference,
        val section: WikiSection
) : OrchidResource(reference) {

    override fun getContentStream(): InputStream {
        val wikiBookTemplate = reference.context.getTemplateResourceSource(null, reference.context.theme).getResourceEntry(reference.context, "wiki/book")!!
        val pdfOutput = wikiBookTemplate.compileContent(reference.context, mapOf(
            "section" to section,
            "resource" to this@WikiBookResource
        ))

        val doc = Jsoup.parse(pdfOutput)
        val pdfDoc = W3CDom().fromJsoup(doc)

        return convertOutputStream(reference.context) { safeOs ->
            PdfRendererBuilder()
                .useSVGDrawer(BatikSVGDrawer())
                .withW3cDocument(pdfDoc, reference.context.baseUrl)
                .toStream(safeOs)
                .run()
        }
    }

    fun replaceBaseUrls(input: String): String {
        val pattern = "href=\"(${Pattern.quote(reference.context.baseUrl)}(.*?))\"".toRegex()

        return pattern.replace(input) { match ->
            val formattedId = match.groupValues[2].replace("/", "_")
            "href=\"#$formattedId\""
        }
    }

    fun formatAnchor(input: String): String {
        return input.replace(reference.context.baseUrl, "").replace("/", "_")
    }
}
