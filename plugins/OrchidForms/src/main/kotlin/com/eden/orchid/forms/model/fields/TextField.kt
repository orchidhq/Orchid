package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.forms.model.FormField
import javax.inject.Inject

@Description("A simple text field, which may optionally be any of tht HTML5 textual input types.", name="Text")
class TextField
@Inject
constructor(
        context: OrchidContext
) : FormField(context, arrayOf(
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
