package com.eden.orchid.plugindocs.tags

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.OptionsDescription
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Option
import javax.inject.Inject

class PluginDocsTag
@Inject constructor(val extractor: OptionsExtractor)
    : TemplateTag("docs", false) {

    @Option
    lateinit var className: String

    override fun parameters(): Array<String> {
        return arrayOf("className")
    }

    fun findClass(): Class<*> {
        return Class.forName(className)
    }

    val classOptions: List<OptionsDescription>
        get() {
            try {
                val classOptions = extractor.describeOptions(findClass())
                classOptions.filter { EdenUtils.isEmpty(it.description) }.forEach {
                    Clog.w("Option has no description: ${it.key} in $className")
                }

                return classOptions
            }
            catch (e: Exception) {

            }

            return emptyList()
        }
}



