package com.eden.orchid.forms.model.fields

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.forms.model.FormField

@Description("A hidden field, to submit data with the form without showing the user.", name="Hidden")
class HiddenField : FormField(arrayOf("hidden")) {

    @Option
    @Description("The value of this hidden field.")
    lateinit var value: String

    override fun getTemplates(): List<String> {
        return super.getTemplates().toMutableList()
    }

}
