package com.eden.orchid.posts.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.ValidationError
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.validateNotBlank
import com.eden.orchid.api.theme.models.Social
import com.eden.orchid.posts.pages.AuthorPage
import java.net.URLEncoder
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

class Author : OptionsHolder {

    @Option
    @Description("The author's name. Authors are referenced by this name.")
    lateinit var name: String

    @Option
    @Description(
        "A fully-specified URL to an avatar image, or a relative path to an Orchid image, used as a " +
            "Gravatar default image."
    )
    var avatar: String = ""
        get() {
            if (email.isNotBlank()) {
                val hash = MessageDigest
                    .getInstance("MD5")
                    .digest(email.trim().toLowerCase().toByteArray(UTF_8))
                    .toHex()

                var gravatarUrl = "https://www.gravatar.com/avatar/${hash.toLowerCase()}"

                if (gravatarDefault.isNotBlank()) {
                    gravatarUrl += "?d=${URLEncoder.encode(gravatarDefault, "UTF-8")}"
                } else if (field.isNotBlank()) {
                    gravatarUrl += "?d=${URLEncoder.encode(field, "UTF-8")}"
                }

                return gravatarUrl
            } else if (!EdenUtils.isEmpty(field)) {
                return field
            }

            return ""
        }

    @Option
    @Description("The author's email address, used to display a Gravatar.")
    lateinit var email: String

    @Option
    @Description(
        "If no avatar is set, a specific Gravatar default, generated image may be used instead. One of " +
            "[404, mm, identicon, monsterid, wavatar, retro, robohash, blank]"
    )
    lateinit var gravatarDefault: String

    @Option
    @Description("The author's social media links.")
    lateinit var social: Social

    var authorPage: AuthorPage? = null

    val link: String
        get() {
            return authorPage?.link ?: ""
        }

    override fun validate(context: OrchidContext): List<ValidationError> {
        return super.validate(context) + listOfNotNull(
            validateNotBlank("name", name)
        )
    }

    private fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
}
