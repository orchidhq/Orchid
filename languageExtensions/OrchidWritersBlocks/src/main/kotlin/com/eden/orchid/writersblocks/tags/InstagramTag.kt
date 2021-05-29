package com.eden.orchid.writersblocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.utilities.resolve
import com.eden.orchid.writersblocks.tags.InstagramTag.Companion.DEPRECATION_MESSAGE
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

@Deprecated(DEPRECATION_MESSAGE)
@Description("Embed an Instagram post in your content.", name = "Instagram")
class InstagramTag : TemplateTag("instagram", Type.Simple, true) {

    companion object {
        const val DEPRECATION_MESSAGE = "Changes to the Instagram oEmbed API have rendered the Orchid Instagram tag " +
                "infeasible. It is advised to use the Embed Button instead. View the documentation here: " +
                "https://developers.facebook.com/docs/instagram/embed-button"
    }

    @Option
    @Description("The Id of an Instagram post to link to.")
    lateinit var id: String

    private val client: OkHttpClient by lazy { context.resolve<OkHttpClient>() }

    override fun parameters() = arrayOf(::id.name)

    val embeddedPost: String? by lazy {
        context.deprecationMessage { DEPRECATION_MESSAGE }
        ""
    }
}
