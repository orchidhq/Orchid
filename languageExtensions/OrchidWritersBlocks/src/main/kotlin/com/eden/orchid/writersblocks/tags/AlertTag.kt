package com.eden.orchid.writersblocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import javax.inject.Inject

class AlertTag @Inject
constructor() : TemplateTag("alert", TemplateTag.Type.Content, true) {

    @Option @StringDefault("info")
    @Description("The Alert level. Typically based on the Bootstrap alert levels of [success, info, warning, and " +
            "danger], but ultimately is up to the theme to determine which levels are available."
    )
    lateinit var level: String

    @Option @StringDefault("")
    @Description("An optional headline to include in the alert, above the content body.")
    lateinit var headline: String

    override fun parameters(): Array<String> {
        return arrayOf("level", "headline")
    }
}
