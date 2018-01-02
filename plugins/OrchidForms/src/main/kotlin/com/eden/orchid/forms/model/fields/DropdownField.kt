package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.forms.model.FormField
import org.json.JSONObject
import javax.inject.Inject

class DropdownField @Inject
constructor(context: OrchidContext) : FormField(context) {

    @Option
    @Description("A map of values which populate the select element.")
    lateinit var options: JSONObject

    override fun getTemplates(): List<String> {
        val allTemplates = super.getTemplates().toMutableList()
        allTemplates.add("fields/dropdown")
        return allTemplates
    }

    override fun acceptsType(type: String): Boolean {
        when (type) {
            "dropdown" -> return true
            else -> return false
        }
    }

}
