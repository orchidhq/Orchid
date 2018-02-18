package com.eden.orchid.javadoc.functions

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.javadoc.models.JavadocModel
import javax.inject.Inject

class JavadocLinkFunction
@Inject
constructor(val model: JavadocModel) : TemplateFunction("javadocLink", true) {

    @Option
    @Description("The name of a class to link to. When linking to an internal page, the simple class name may be " +
            "used if it is unique across all packages. Searching by a fully-qualified class name will first search " +
            "local class pages, but will fall back to looking in the external index for a matching page. This allows " +
            "your classes to intelligently link to the Orchid documentation of other sites."
    )
    lateinit var className: String

    @Option
    @Description("The text to display in the link.")
    lateinit var linkText: String

    @Option @BooleanDefault(false)
    @Description("Whether to render the link as an anchor or just return the resulting URL.")
    var anchor: Boolean = false

    override fun parameters(): Array<String> {
        return arrayOf("className", "linkText", "anchor")
    }

    override fun apply(input: Any?): Any {
        return model.linkClassPage(input?.toString() ?: className, linkText, anchor)
    }
}