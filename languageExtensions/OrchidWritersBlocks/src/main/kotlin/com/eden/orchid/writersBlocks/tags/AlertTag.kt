package com.eden.orchid.writersBlocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import javax.inject.Inject

class AlertTag @Inject
constructor() : TemplateTag("alert", true) {

    @Option @StringDefault("info")
    lateinit var level: String

    @Option
    @StringDefault("")
    lateinit var headline: String

    override fun parameters(): Array<String> {
        return arrayOf("level", "headline")
    }
}
