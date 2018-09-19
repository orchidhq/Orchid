package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.forms.model.FormField
import org.json.JSONObject
import javax.inject.Inject

@Description("A dropdown selector, with a list of key-value pairs as options.", name="Dropdown")
class DropdownField
@Inject
constructor(
        context: OrchidContext
) : FormField(context, arrayOf("dropdown")) {

    @Option
    @Description("A map of values which populate the select element.")
    lateinit var options: JSONObject

    @Option @BooleanDefault(false)
    @Description("Whether to allow multi-select of options.")
    var multiple: Boolean = false

    override fun getTemplates(): List<String> {
        return super.getTemplates().toMutableList()
    }

}
