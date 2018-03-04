package com.eden.orchid.posts.functions

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.utils.PostsExcerptStrategy
import javax.inject.Inject

class ExcerptFunction @Inject
constructor(val strategy: PostsExcerptStrategy) : TemplateFunction("excerpt", false) {

    override fun parameters(): Array<String> {
        return emptyArray()
    }

    override fun apply(input: Any?): Any {
        if(input != null && input is OrchidPage) {
            return strategy.getExcerpt(input)
        }

        return ""
    }

}
