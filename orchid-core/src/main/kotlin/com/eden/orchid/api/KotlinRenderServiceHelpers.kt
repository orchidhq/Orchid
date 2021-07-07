package com.eden.orchid.api

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.asInputStream
import com.eden.orchid.utilities.convertOutputStream
import com.eden.orchid.utilities.resolve
import com.openhtmltopdf.extend.FSStream
import com.openhtmltopdf.extend.FSStreamFactory
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import com.openhtmltopdf.svgsupport.BatikSVGDrawer
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.helper.W3CDom
import org.jsoup.nodes.Element
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.Reader

object KotlinRenderServiceHelpers {

    @Synchronized
    fun getRenderedPdf(page: OrchidPage): InputStream {
        // render the main content of the page (not in a layout)
        page.isCurrent = true
        val htmlContent = "" + page.renderContent()
        page.isCurrent = false

        // parse the main content to a Jsoup HTML document
        val doc = Jsoup.parse(htmlContent)

        // rewrite IDs and anchors to conform to PDF specifications
        val baseUrl = page.context.baseUrl
        for (el in doc.select("*[id],*[href],*[src]")) {
            if (el.attr("id").startsWith(baseUrl)) {
                // hash ID to be an internal ID
                el.hashIt("id")
            } else if (el.attr("href").startsWith(baseUrl)) {
                // hash href to point to an internal ID
                el.hashIt("href", prefix = "#")
            } else if (el.attr("src").startsWith(baseUrl)) {
                // replace URL scheme with orchid:// so we can fetch it internally
                el.prefixIt("src", prefix = "orchid://")
            }
        }

        // create and configure OpenHtmlToPdf library to convert the Jsoup document to PDF
        val pdfBuilder = PdfRendererBuilder()
            .useSVGDrawer(BatikSVGDrawer())
            .withW3cDocument(W3CDom().fromJsoup(doc), page.context.baseUrl)
            .useHttpStreamImplementation(RenderedSiteStreamFactory(page.context))
            .useProtocolsStreamImplementation(RenderedSiteStreamFactory(page.context), "orchid")

        // pipe the HTML document to OpenHtmlToPDF
        val pdfInputStream = convertOutputStream(page.context) { safeOs ->
            pdfBuilder.toStream(safeOs).run()
        }

        // return the stream to write to file
        return pdfInputStream
    }

    private fun Element.hashIt(attrName: String, prefix: String = "", suffix: String = "") {
        val hashedAttribute = OrchidUtils.sha1(attr(attrName))
        attr(attrName, "$prefix$hashedAttribute$suffix")
    }

    private fun Element.prefixIt(attrName: String, prefix: String = "", suffix: String = "") {
        attr(attrName, "$prefix${attr(attrName)}$suffix")
    }
}

internal class RenderedSiteStreamFactory(
    val context: OrchidContext
) : FSStreamFactory {
    override fun getUrl(url: String): FSStream {
        val isOrchidUrl = url.startsWith("orchid://")
        if (isOrchidUrl) {
            val filePath = url.removePrefix("orchid://").removePrefix(context.baseUrl)
            val locatedFile = File(context.destinationDir).resolve(filePath)
            return FileFSStream(locatedFile)
        } else {
            val client = context.resolve<OkHttpClient>()
            val request: Request = Request.Builder()
                .url(url)
                .build()

            val response: Response = client.newCall(request).execute()
            return OkHttpFSStream(response)
        }
    }
}

class FileFSStream(
    val file: File
) : FSStream {
    override fun getStream(): InputStream {
        return try {
            FileInputStream(file)
        } catch (e: Exception) {
            e.printStackTrace()
            "".asInputStream()
        }
    }

    override fun getReader(): Reader {
        return file.bufferedReader()
    }
}

class OkHttpFSStream(
    val response: Response
) : FSStream {
    override fun getStream(): InputStream {
        return response.body!!.byteStream()
    }

    override fun getReader(): Reader {
        return response.body!!.charStream()
    }
}
