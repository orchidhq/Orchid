package com.eden.orchid.search.model.fields

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.search.model.FormField
import javax.inject.Inject

@Description("A hidden field, to submit data with the form without showing the user.", name="Hidden")
class HiddenField
@Inject
constructor(
        context: OrchidContext
) : FormField(context, arrayOf("hidden")) {

    @Option
    @Description("The value of this hidden field.")
    lateinit var value: String

    override fun getTemplates(): List<String> {
        return super.getTemplates().toMutableList()
    }

}
