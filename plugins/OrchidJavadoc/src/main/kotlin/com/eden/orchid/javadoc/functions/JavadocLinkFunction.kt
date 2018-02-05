package com.eden.orchid.javadoc.functions

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.javadoc.JavadocModel
import javax.inject.Inject

class JavadocLinkFunction
@Inject
constructor(val model: JavadocModel) : TemplateFunction("javadocLink") {

    @Option
    lateinit var className: String

    @Option
    lateinit var linkText: String

    @Option @BooleanDefault(false)
    var anchor: Boolean = false

    override fun parameters(): Array<String> {
        return arrayOf("className", "linkText", "anchor")
    }

    override fun isSafe(): Boolean {
        return true
    }

    override fun apply(input: Any?): Any {
        val page = model.getClassPage(input?.toString() ?: className)
        if(page != null) {
            if(!EdenUtils.isEmpty(linkText)) {
                if (anchor) {
                    return "<a href=\"${page.link}\">$linkText</a>"
                } else {
                    return "<span>$linkText</span>"
                }
            }
            else {
                return page.link
            }
        }

        return linkText
    }
}