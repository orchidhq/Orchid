package com.eden.orchid.impl.relations

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Relation
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.Theme
import javax.inject.Inject

class ThemeRelation
@Inject
constructor(
    context: OrchidContext
) : Relation<Theme>(context) {

    @Option
    @Description("The theme Key to load.")
    lateinit var key: String

    @AllOptions
    lateinit var allData: Map<String, Any>

    override fun load(): Theme? {
        return if (key.isNotBlank()) {
            context.findTheme(key)?.also { it.extractOptions(context, allData) }
        } else {
            null
        }
    }

    override fun parseStringRef(ref: String?): Map<String, Any> {
        return mapOf("key" to (ref ?: ""))
    }

    override fun toString(): String {
        val item = get()
        return "ThemeRelation(key='$key', allData='$allData', item='$item')"
    }
}
