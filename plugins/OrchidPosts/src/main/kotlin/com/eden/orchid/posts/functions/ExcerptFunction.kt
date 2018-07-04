package com.eden.orchid.posts.functions

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.utils.PostsExcerptStrategy
import javax.inject.Inject

class ExcerptFunction @Inject
constructor(val strategy: PostsExcerptStrategy) : TemplateFunction("excerpt", false) {

    var page: Any? = null

    override fun parameters(): Array<String> {
        return arrayOf("page")
    }

    override fun apply(): Any {
        if(page != null && page is OrchidPage) {
            return strategy.getExcerpt(page as OrchidPage)
        }

        return ""
    }

}
