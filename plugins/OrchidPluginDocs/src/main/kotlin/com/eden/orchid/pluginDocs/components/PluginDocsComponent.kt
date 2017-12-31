package com.eden.orchid.pluginDocs.components

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsDescription
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import javax.inject.Inject

class PluginDocsComponent
@Inject constructor(context: OrchidContext, val extractor: OptionsExtractor)
    : OrchidComponent(context, "pluginDocs", 25) {

    @Option
    var className: String? = null

    @Option
    var classNames: Array<String>? = null

    fun getClassList(): Set<Class<*>> {
        val classList = emptyList<Class<*>>().toMutableSet()

        if(!EdenUtils.isEmpty(className)) {
            try {
                classList.add(Class.forName(className!!))
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if(!EdenUtils.isEmpty(classNames)) {
            classNames!!.forEach {
                try {
                    classList.add(Class.forName(it))
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return classList
    }

    fun getClassOptions(itemClass: Class<*>): List<OptionsDescription> {
        val classOptions = extractor.describeOptions(itemClass)

        classOptions.filter { EdenUtils.isEmpty(it.description) }.forEach {
            Clog.w("Option has no description: ${it.key} in ${itemClass.name}")
        }

        return classOptions
    }
}


