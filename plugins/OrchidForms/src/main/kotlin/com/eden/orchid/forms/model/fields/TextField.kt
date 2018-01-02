package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.forms.model.FormField
import javax.inject.Inject

class TextField @Inject
constructor(context: OrchidContext) : FormField(context) {

    override fun getTemplates(): List<String> {
        val allTemplates = super.getTemplates().toMutableList()
        allTemplates.add("fields/textual")
        return allTemplates
    }

    override fun acceptsType(type: String): Boolean {
        when (type) {
            "text", "number", "email" -> return true
            else -> return false
        }
    }

}
