package com.eden.orchid.snippets.adapter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.ExternalResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.snippets.models.SnippetConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

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

    @Option
    @Description("A list of multiple selectors to turn into snippets within the single webpage")
    lateinit var selectors: List<RemoteSnippetSelectorConfig>

    override fun addSnippets(context: OrchidContext): Sequence<SnippetConfig> {
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

        val doc = Jsoup.parse(bodyString)

        return if(selectors.isNotEmpty()) {
            loadSelectorsAsSnippets(
                context,
                doc,
                selectors
            )
        } else {
            loadSelectorsAsSnippets(
                context,
                doc,
                listOf(
                    RemoteSnippetSelectorConfig().apply {
                        selector = this@RemoteSnippetsAdapter.selector
                        name = this@RemoteSnippetsAdapter.name
                        tags = emptyList()
                    }
                )
            )
        }
    }

    private fun loadSelectorsAsSnippets(
        context: OrchidContext,
        document: Document,
        selectors: List<RemoteSnippetSelectorConfig>
    ): Sequence<SnippetConfig> = sequence {
        selectors.forEach {
            yield(
                SnippetConfig(
                    context,
                    it.name,
                    it.tags,
                    StringResource(
                        OrchidReference.fromUrl(context, "", url),
                        document.select(it.selector).html()
                    )
                )
            )
        }
    }

    class RemoteSnippetSelectorConfig : OptionsHolder {

        @Option
        @Description("The selector on the webpage to extract into a snippet")
        lateinit var selector: String

        @Option
        @Description("The name of this snippet")
        lateinit var name: String

        @Option
        @Description("Tags for this selector")
        lateinit var tags: List<String>
    }
}
