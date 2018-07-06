package com.eden.orchid.plugindocs.tags

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.krow.formatters.HtmlTableFormatter
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.OptionsDescription
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import javax.inject.Inject

class PluginDocsTag
@Inject constructor(val extractor: OptionsExtractor)
    : TemplateTag("docs", TemplateTag.Type.Simple, true) {

    @Option
    @Description("A fully-qualified class name to render options for.")
    lateinit var className: String

    @Option @BooleanDefault(true)
    @Description("Whether to include the class's own options.")
    var includeOwnOptions: Boolean = true

    @Option @BooleanDefault(true)
    @Description("Whether to include the options the class inherits from its parent classes.")
    var includeInheritedOptions: Boolean = true

    override fun parameters(): Array<String> {
        return arrayOf("className", "includeOwnOptions", "includeInheritedOptions")
    }

    fun findClass(): Class<*> {
        return Class.forName(className)
    }

    val classOptions: List<OptionsDescription>
        get() {
            try {
                val classOptions = extractor.describeOptions(findClass(), includeOwnOptions, includeInheritedOptions).optionsDescriptions
                classOptions.filter { EdenUtils.isEmpty(it.description) }.forEach {
                    Clog.w("Option has no description: ${it.key} in $className")
                }

                return classOptions
            }
            catch (e: Exception) {

            }

            return emptyList()
        }

    public fun getOptions(includeOwnOptions: Boolean, includeInheritedOptions: Boolean): String {
        val description = extractor.describeOptions(findClass(), includeOwnOptions, includeInheritedOptions)
        val table = extractor.getDescriptionTable(description)

        var htmlTable = table.print(HtmlTableFormatter())
        htmlTable = htmlTable.replace("<table>".toRegex(), "<table class=\"table\">")

        return htmlTable
    }
}



