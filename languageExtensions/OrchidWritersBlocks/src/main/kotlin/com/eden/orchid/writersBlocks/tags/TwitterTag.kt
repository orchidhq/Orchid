package com.eden.orchid.writersBlocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import javax.inject.Inject

class TwitterTag @Inject
constructor(val client: OkHttpClient) : TemplateTag("twitter", false) {

    @Option
    lateinit var user: String

    @Option
    lateinit var id: String

    @Option
    lateinit var timelineType: String

    @Option
    lateinit var listId: String

    @Option
    lateinit var collectionId: String

    @Option
    lateinit var title: String

    @Option
    lateinit var momentId: String

    override fun parameters(): Array<String> {
        return arrayOf("user", "id")
    }

    val embeddedTweet: String
        get() {
            val tweetUrl = "https://twitter.com/$user/status/$id"
            val requestUrl = "https://publish.twitter.com/oembed?url=${URLEncoder.encode(tweetUrl, "UTF-8")}"

            val request = Request.Builder().url(requestUrl.trim()).build()

            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val body = response.body()?.string() ?: ""
                    return JSONObject(body).getString("html")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return ""
        }

}
