package com.eden.orchid.api.resources.resource

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import kotlin.properties.Delegates

/**
 * An ExternalResource acts either as a placeholder for URL reference, or can be a full resource when set to download.
 * When an ExternalResource is set to download, the file at the original URL will be downloaded and cached when its
 * contents are requested, so that external pages or assets can be requested at build-time from a CDN or other external
 * location, but served locally so your output site has no dependency on that external service. By default,
 * ExternalResources are set to not download, and instead just point to the desired resource.
 */
class ExternalResource(
    reference: OrchidReference
) : OrchidResource(reference) {

    var download: Boolean by Delegates.observable(false) { _, _, it ->
        if (it) {
            reference.baseUrl = reference.context.baseUrl
        } else {
            reference.baseUrl = originalExternalReference.baseUrl
        }
        free()
    }

    private val originalExternalReference: OrchidReference = OrchidReference(reference)

    init {
        originalExternalReference.isUsePrettyUrl = false
        download = false
    }

    override fun shouldPrecompile(): Boolean {
        return if (download) {
            super.shouldPrecompile()
        } else {
            false
        }
    }

    override fun shouldRender(): Boolean {
        return download
    }

    @Throws(Exception::class)
    private fun getContentBody(): InputStream? {
        if (download) {
            Clog.i("Downloading external resource {}", originalExternalReference.toString())
            val client = reference.context.resolve(OkHttpClient::class.java)
            val request = Request.Builder().url(originalExternalReference.toString()).build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body
                if (responseBody != null) {
                    return responseBody.byteStream()
                }
            }
            return null
        } else {
            throw UnsupportedOperationException("This method is not allowed on ExternalResource when it is not set to download")
        }
    }

    override fun getContentStream(): InputStream {
        try {
            val inputStream = getContentBody()
            if (inputStream != null) return inputStream
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "".asInputStream()
    }

    override val content: String
        get() = if (download) {
            super.content
        } else {
            throw UnsupportedOperationException("This method is not allowed on ExternalResource when it is not set to download")
        }

    override val embeddedData: Map<String, Any?>
        get() = if (download) super.embeddedData else emptyMap()

    fun isDownload(): Boolean {
        return download
    }
}
