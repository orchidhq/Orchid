package com.eden.orchid.javadoc.functions

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.javadoc.models.JavadocModel
import javax.inject.Inject

class JavadocLinkFunction
@Inject
constructor(val model: JavadocModel) : TemplateFunction("javadocLink", true) {

    @Option
    lateinit var className: String

    @Option
    lateinit var linkText: String

    @Option @BooleanDefault(false)
    var anchor: Boolean = false

    override fun parameters(): Array<String> {
        return arrayOf("className", "linkText", "anchor")
    }

    override fun apply(input: Any?): Any {
        return model.linkClassPage(input?.toString() ?: className, linkText, anchor)
    }
}