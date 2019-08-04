package com.eden.orchid.writersblocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.utilities.SuppressedWarnings
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import javax.inject.Inject

@Description("Embed an Instagram post in your content.", name = "Instagram")
class InstagramTag
@Inject
constructor(
        val client: OkHttpClient
) : TemplateTag("instagram", TemplateTag.Type.Simple, true) {

    @Option
    @Description("The Id of an Instagram post to link to.")
    lateinit var id: String

    override fun parameters(): Array<String> {
        return arrayOf("id")
    }

    var embeddedPost: String? = null
        @Suppress(SuppressedWarnings.UNUSED_PARAMETER)
        private set(value) {
        }
        get() {
            if (field == null) {
                val postUrl = "http://instagr.am/p/$id"
                val requestUrl = "https://api.instagram.com/oembed?url=${URLEncoder.encode(postUrl, "UTF-8")}"

                val request = Request.Builder().url(requestUrl.trim()).build()

                try {
                    val response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        val body = response.body?.string() ?: ""
                        field = JSONObject(body).getString("html")
                    }
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            return field
        }

}
