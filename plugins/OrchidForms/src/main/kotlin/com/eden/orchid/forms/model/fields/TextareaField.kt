package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.forms.model.FormField
import javax.inject.Inject

class TextareaField @Inject
constructor(context: OrchidContext) : FormField(context, arrayOf("textarea")) {

    @Option
    @Description("The initial height of the textarea in rows.")
    var rows: Int = 0

    @Option
    @Description("The initial width of the textarea in average character columns.")
    var cols: Int = 0

    override fun getTemplates(): List<String> {
        val allTemplates = super.getTemplates().toMutableList()
        allTemplates.add("fields/textual")
        return allTemplates
    }

}
