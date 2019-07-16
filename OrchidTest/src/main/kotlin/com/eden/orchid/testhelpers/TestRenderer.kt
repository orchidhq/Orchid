package com.eden.orchid.testhelpers

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.render.OrchidRenderer
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.nio.charset.Charset
import javax.inject.Singleton

@Singleton
class TestRenderer : OrchidRenderer {

    private val renderedPageMap = mutableMapOf<String, TestRenderedPage>()

    override fun render(page: OrchidPage, content: InputStream): Boolean {
        val outputPath = OrchidUtils.normalizePath(page.reference.path)
        val outputName: String?
        if (EdenUtils.isEmpty(OrchidUtils.normalizePath(page.reference.outputExtension))) {
            outputName = OrchidUtils.normalizePath(page.reference.fileName)
        } else {
            outputName =
                OrchidUtils.normalizePath(page.reference.fileName) + "." + OrchidUtils.normalizePath(page.reference.outputExtension)
        }

        val fullFilePath = "/" + OrchidUtils.normalizePath("$outputPath/$outputName")

        val outputFile = TestRenderedPage(fullFilePath, content, page)
        renderedPageMap[outputFile.path] = outputFile
        return true
    }

    data class TestRenderedPage(
        val path: String,
        private val contentStream: InputStream,
        val origin: OrchidPage
    ) {
        val content: String by lazy {
            IOUtils.toString(contentStream, Charset.forName("UTF-8"))
        }

        override fun toString(): String {
            return "TestRenderer.TestRenderedPage(path=" + this.path + ", origin=" + this.origin + ", content=" + this.content + ")"
        }
    }

    fun getRenderedPageMap(): Map<String, TestRenderedPage> {
        return this.renderedPageMap
    }
}
