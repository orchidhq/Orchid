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

@Description("Embed an Instagram post in your content.", name = "Instagram")
class InstagramTag : TemplateTag("instagram", Type.Simple, true) {

    @Option
    @Description("The Id of an Instagram post to link to.")
    lateinit var id: String

    private val client: OkHttpClient by lazy { context.resolve<OkHttpClient>() }

    override fun parameters(): Array<String> {
        return arrayOf("id")
    }

    val embeddedPost: String? by lazy {
        val postUrl = "http://instagr.am/p/$id"
        val requestUrl = "https://api.instagram.com/oembed?url=${URLEncoder.encode(postUrl, "UTF-8")}"

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
