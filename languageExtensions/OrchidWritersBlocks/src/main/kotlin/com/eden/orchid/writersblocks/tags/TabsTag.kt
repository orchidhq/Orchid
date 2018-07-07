package com.eden.orchid.writersblocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import javax.inject.Inject

class TabsTag @Inject
constructor() : TemplateTag("tabs", TemplateTag.Type.Tabbed, true) {

    override fun parameters(): Array<String> {
        return arrayOf()
    }

    override fun getNewTab(key: String?, content: String?): TemplateTag.Tab {
        return Tab(key, content)
    }

    class Tab(private val key: String?, private val content: String?) : TemplateTag.Tab {

        @Option
        @Description("The title of the tab")
        lateinit var title: String

        override fun getKey(): String? {
            return key
        }

        override fun getContent(): String? {
            return content
        }

        override fun parameters(): Array<String> {
            return arrayOf("title")
        }

    }

}
