package com.eden.orchid.api.resources.resource

import clog.Clog
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

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

    private val isProd = reference.context.isProduction
    var downloadInProdOnly = true
    var download: Boolean = false

    val shouldDownload: Boolean get() = if (downloadInProdOnly) isProd && download else download

    override fun shouldPrecompile(): Boolean {
        return if (shouldDownload) {
            super.shouldPrecompile()
        } else {
            false
        }
    }

    override fun shouldRender(): Boolean {
        return shouldDownload
    }

    @Throws(Exception::class)
    private fun getContentBody(): InputStream? {
        if (shouldDownload) {
            Clog.i("Downloading external resource {}", reference.toString(reference.context))
            val client = reference.context.resolve(OkHttpClient::class.java)
            val request = Request.Builder().url(reference.toString(reference.context)).build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body
                if (responseBody != null) {
                    return responseBody.byteStream()
                }
            }
            return null
        } else {
            throw UnsupportedOperationException(
                "This method is not allowed on ExternalResource when it is not set " +
                    "to download"
            )
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
        get() = if (shouldDownload) {
            super.content
        } else {
            throw UnsupportedOperationException(
                "This method is not allowed on ExternalResource when it is not set " +
                    "to download"
            )
        }

    override val embeddedData: Map<String, Any?>
        get() = if (shouldDownload) super.embeddedData else emptyMap()
}
