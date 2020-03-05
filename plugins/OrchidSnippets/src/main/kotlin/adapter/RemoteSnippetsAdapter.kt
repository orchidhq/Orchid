package com.eden.orchid.snippets.adapter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.ExternalResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.snippets.models.SnippetConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

@Description("Download a remote file over HTTP and locate snippets on that page either through regex or a CSS selector")
class RemoteSnippetsAdapter(

) : SnippetsAdapter {

    override fun getType(): String = "remote"

    @Option
    @Description("URL to a webpage to download and extract a snippet from")
    lateinit var url: String

    @Option
    @Description("The selector on the webpage to extract into a snippet")
    lateinit var selector: String

    @Option
    @Description("The name of this snippet")
    lateinit var name: String

    override fun addSnippets(context: OrchidContext): Sequence<SnippetConfig> = sequence {
        val client = context.resolve(OkHttpClient::class.java)

        val bodyString = client
            .newCall(
                Request.Builder()
                    .url(url)
                    .get()
                    .build()
            )
            .execute()
            .body!!.string()

        yield(
            SnippetConfig(
                context,
                name,
                emptyList<String>(),
                StringResource(
                    OrchidReference.fromUrl(context, "", url),
                    Jsoup.parse(bodyString).select(selector).html(),
                    null
                )
            )
        )
    }
}
