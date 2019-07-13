package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import org.apache.commons.lang3.RandomStringUtils
import javax.inject.Inject

@Description("Display multiple content sections in selectable tabs.", name = "Tabs")
class TabsTag
@Inject
constructor() : TemplateTag("tabs", Type.Tabbed, true) {

    @Option
    @Description("The unique id of this accordion. Defaults to a random value.")
    var id: String = ""
        get() {
            if (field.isBlank()) field = RandomStringUtils.random(10, true, true)
            return field
        }

    override fun parameters(): Array<String> {
        return arrayOf("id")
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
