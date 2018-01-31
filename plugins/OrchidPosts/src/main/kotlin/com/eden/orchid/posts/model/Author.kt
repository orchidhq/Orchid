package com.eden.orchid.posts.model

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.ApplyBaseUrl
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.posts.pages.AuthorPage
import java.net.URLEncoder
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

class Author : OptionsHolder {

    @Option
    lateinit var name: String

    @Option
    @ApplyBaseUrl
    lateinit var avatar: String

    @Option
    lateinit var email: String

    @Option
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

