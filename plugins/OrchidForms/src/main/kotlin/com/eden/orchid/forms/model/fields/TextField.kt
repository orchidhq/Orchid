package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.forms.model.FormField
import javax.inject.Inject

class TextField @Inject
constructor(context: OrchidContext) : FormField(context, arrayOf(
        "text",
        "password",
        "datetime",
        "datetime-local",
        "date",
        "month",
        "time",
        "week",
        "number",
        "email",
        "url",
        "search",
        "tel",
        "color"
)) {

    override fun getTemplates(): List<String> {
        val allTemplates = super.getTemplates().toMutableList()
        allTemplates.add("fields/textual")
        return allTemplates
    }

}
