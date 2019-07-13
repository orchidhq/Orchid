package com.eden.orchid.impl.themes.tags

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import javax.inject.Inject

@Description(value = "Print the tag content to the console for debugging templates.", name = "Log")
class LogTag @Inject
constructor() : TemplateTag("log", TemplateTag.Type.Content, false) {

    @Option
    @StringDefault("verbose")
    @Description("The priority with which to log the message.")
    lateinit var level: String

    @Option
    @Description("An options tag to display with the message.")
    lateinit var tag: String

    override fun parameters(): Array<String> {
        return arrayOf("level", "tag")
    }

    override fun onRender(context: OrchidContext?, page: OrchidPage?) {
        val clog = if (!EdenUtils.isEmpty(tag)) Clog.tag(tag) else Clog.getInstance()

        when (level.toLowerCase()) {
            "verbose" -> clog.v(content)
            "debug" -> clog.d(content)
            "info" -> clog.i(content)
            "warning" -> clog.w(content)
            "error" -> clog.e(content)
            "fatal" -> clog.wtf(content)
            else -> clog.v(content)
        }
    }
}
