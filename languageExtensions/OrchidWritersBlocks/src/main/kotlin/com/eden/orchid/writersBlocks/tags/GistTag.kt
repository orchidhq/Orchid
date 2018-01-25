package com.eden.orchid.writersBlocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import javax.inject.Inject

class GistTag @Inject
constructor() : TemplateTag("gist", false) {

    @Option
    lateinit var user: String

    @Option
    lateinit var id: String

    @Option
    lateinit var file: String

    override fun parameters(): Array<String> {
        return arrayOf("user", "id", "file")
    }
}
