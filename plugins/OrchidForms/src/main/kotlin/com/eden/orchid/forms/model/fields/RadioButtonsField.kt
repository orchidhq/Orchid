package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.forms.model.FormField
import org.json.JSONObject

@Description("A list of radio buttons with key-value pairs as options.", name = "Radio Buttons")
class RadioButtonsField : FormField(arrayOf("radio")) {

    @Option
    @Description("A map of values which populate the radio buttons.")
    lateinit var options: JSONObject

    override fun getTemplates(): List<String> {
        return super.getTemplates().toMutableList()
    }

}
