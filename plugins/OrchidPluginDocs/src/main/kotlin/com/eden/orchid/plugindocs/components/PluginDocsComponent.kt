package com.eden.orchid.plugindocs.components

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import javax.inject.Inject

class PluginDocsComponent
@Inject constructor(context: OrchidContext)
    : OrchidComponent(context, "pluginDocs", 25) {

    @Option
    @Description("A list of fully-qualified class names to render options for.")
    var classNames: Array<String>? = null

    fun getClassList(): Set<String> {
        val classList = emptyList<String>().toMutableSet()

        if (!EdenUtils.isEmpty(classNames)) {
            classNames!!.forEach {
                classList.add(it)
            }
        }

        return classList
    }
}



