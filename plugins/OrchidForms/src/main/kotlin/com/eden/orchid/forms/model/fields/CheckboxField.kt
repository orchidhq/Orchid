package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.forms.model.FormField
import javax.inject.Inject

@Description("A checkbox, for boolean true/false input.", name="Checkbox")
class CheckboxField
@Inject
constructor(
        context: OrchidContext
) : FormField(context, arrayOf("checkbox")) {

    @Option
    @Description("The key the field maps to in the resulting form")
    var default: Boolean = false

    override fun getTemplates(): List<String> {
        return super.getTemplates().toMutableList()
    }

}
