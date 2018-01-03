package com.eden.orchid.posts.model

import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.posts.pages.PostTagArchivePage

data class TagModel(
        val tag: String
) {

    val first: MutableList<PostPage> = ArrayList()
    val second: MutableList<PostTagArchivePage> = ArrayList()
}
