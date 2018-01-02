package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.forms.model.FormField
import javax.inject.Inject

class HiddenField @Inject
constructor(context: OrchidContext) : FormField(context) {

    @Option
    lateinit var value: String

    override fun getTemplates(): List<String> {
        val allTemplates = super.getTemplates().toMutableList()
        allTemplates.add("fields/hidden")
        return allTemplates
    }

    override fun acceptsType(type: String): Boolean {
        when (type) {
            "hidden" -> return true
            else -> return false
        }
    }

}
