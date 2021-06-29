package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.regex.Pattern

@Description("Show a snippet of a page's content.", name = "Excerpt")
class ExcerptFunction : TemplateFunction("excerpt", false) {

    @Option
    var input: Any? = null

    @Option
    @StringDefault("<!--more-->")
    @Description(
        "The shortcode used to manually set the breakpoint for a page summary, otherwise the summary is the " +
            "first 240 characters of the post."
    )
    lateinit var excerptSeparator: String

    override fun parameters() = arrayOf(::input.name, ::excerptSeparator.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        if (input != null && input is OrchidPage) {
            return getExcerpt(input as OrchidPage)
        } else if (page != null) {
            return getExcerpt(page)
        }

        return ""
    }

    private fun getExcerpt(page: OrchidPage): String {
        val content = page.content

        val pattern = Pattern.compile(excerptSeparator, Pattern.DOTALL or Pattern.MULTILINE)

        return if (pattern.matcher(content).find()) {
            pattern.split(content)[0].stripTags()
        } else {
            return content.stripTags().let {
                if (it.length > 240) { it.substring(0, 240) + "..." } else { it }
            }
        }
    }

    private fun String.stripTags(): String {
        return this.replace("(<.*?>)|(&.*?;)|([ ]{2,})".toRegex(), "")
    }
}
