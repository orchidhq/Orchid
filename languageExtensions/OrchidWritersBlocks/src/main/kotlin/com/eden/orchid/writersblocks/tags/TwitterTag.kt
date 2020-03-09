package com.eden.orchid.writersblocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.utilities.resolve
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

@Description("Embed a Twitter post or feed in your page.", name = "Twitter")
class TwitterTag : TemplateTag("twitter", Type.Simple, true) {

    @Option
    @Description("The Twitter handle to use, without the @.")
    lateinit var user: String

    @Option
    @Description("The Id of a single tweet to embed.")
    lateinit var id: String

    @Option
    @Description("The type of Twitter timeline to display. One of [likes, lists, collection, grid, moment, or " +
            "profile]."
    )
    lateinit var timelineType: String

    @Option
    @Description("Only used with `lists` timeline type. The Timeline List Id.")
    lateinit var listId: String

    @Option
    @Description("Only used with `collection` or `grid` timeline types. The Timeline Collection Id.")
    lateinit var collectionId: String

    @Option
    @Description("Only used with `moment` timeline type. The Timeline Moment Id.")
    lateinit var momentId: String

    @Option
    @Description("Only used with `collection`, `grid`, and `moment` timeline types. The Timeline title.")
    lateinit var title: String

    private val client: OkHttpClient by lazy { context.resolve<OkHttpClient>() }

    override fun parameters() = arrayOf(::user.name, ::id.name)

    val embeddedTweet: String? by lazy {
        val tweetUrl = "https://twitter.com/$user/status/$id"
        val requestUrl = "https://publish.twitter.com/oembed?url=${URLEncoder.encode(tweetUrl, "UTF-8")}"

        val request = Request.Builder().url(requestUrl.trim()).build()

        try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body = response.body?.string() ?: ""
                JSONObject(body).getString("html")
            }
            else {
                null
            }
        }
        catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}
