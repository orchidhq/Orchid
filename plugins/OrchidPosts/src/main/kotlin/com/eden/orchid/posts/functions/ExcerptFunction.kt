package com.eden.orchid.posts.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.utils.PostsExcerptStrategy
import javax.inject.Inject

@Description("Show a snippet of a page's content.", name = "Excerpt")
class ExcerptFunction
@Inject
constructor(
        val strategy: PostsExcerptStrategy
) : TemplateFunction("excerpt", false) {

    @Option
    var input: Any? = null

    override fun parameters(): Array<String> {
        return arrayOf("input")
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        if (input != null && input is OrchidPage) {
            return strategy.getExcerpt(input as OrchidPage)
        }
        else if (page != null) {
            return strategy.getExcerpt(page)
        }

        return ""
    }

}
