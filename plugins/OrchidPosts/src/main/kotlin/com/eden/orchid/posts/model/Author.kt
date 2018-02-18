package com.eden.orchid.posts.model

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.ApplyBaseUrl
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.posts.pages.AuthorPage
import java.net.URLEncoder
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

class Author : OptionsHolder {

    @Option
    @Description("The author's name. Authors are referenced by this name.")
    lateinit var name: String

    @Option @ApplyBaseUrl
    @Description("A fully-specified URL to an avatar image, or a relative path to an Orchid image, used as a " +
            "Gravatar default image."
    )
    lateinit var avatar: String

    @Option
    @Description("The author's email address, used to display a Gravatar.")
    lateinit var email: String

    @Option
    @Description("If no avatar is set, a specific Gravatar default, generated image may be used instead. One of " +
            "[404, mm, identicon, monsterid, wavatar, retro, robohash, blank]"
    )
    lateinit var gravatarDefault: String

    var authorPage: AuthorPage? = null

    val avatarUrl: String
        get() {
            if(email.isNotBlank()) {
                val md5 = MessageDigest.getInstance("MD5")
                md5.update(email.trim().toLowerCase().toByteArray())
                val digest = md5.digest()
                val hash = DatatypeConverter.printHexBinary(digest)
                var gravatarUrl = "https://www.gravatar.com/avatar/${hash.toLowerCase()}"

                if(gravatarDefault.isNotBlank()) {
                    gravatarUrl += "?d=${URLEncoder.encode(gravatarDefault, "UTF-8")}"
                }
                else if(avatar.isNotBlank()) {
                    gravatarUrl += "?d=${URLEncoder.encode(avatar, "UTF-8")}"
                }

                return gravatarUrl
            }
            else {
                return avatar
            }
        }

    val link: String
        get() {
            return authorPage?.link ?: ""
        }

}

