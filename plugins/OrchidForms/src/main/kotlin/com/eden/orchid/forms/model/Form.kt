package com.eden.orchid.forms.model

import com.eden.common.json.JSONElement
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ModularListConfig
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import org.json.JSONObject

class Form(val context: OrchidContext, var key: String, formData: JSONObject) : OptionsHolder {

    @Option
    @Description("The user-facing title of the form.")
    lateinit var title: String

    @Option
    @Description("The 'action' attribute of the form. On forms which have a dedicated submission page, if this is " +
            "not provided, it defaults to the URL of the submission page."
    )
    lateinit var action: String

    @Option @StringDefault("POST")
    @Description("The HTTP request method the form submits as. One of ['GET', 'POST'].")
    lateinit var method: String

    @Option
    @Description("A map of arbitrary attributes to add to the form element.")
    lateinit var attributes: JSONObject

    @Option @ModularListConfig(objectKeyName = "key")
    @Description("The fields in this form.")
    lateinit var fields: FormFieldList

    @AllOptions
    lateinit var allData: Map<String, Any>

    init {
        try {
            extractOptions(context, formData)

            if(EdenUtils.isEmpty(key) && !EdenUtils.isEmpty(title)) {
                key = title
            }

            if(EdenUtils.isEmpty(key)) {
                throw IllegalArgumentException("The form must define a 'key' or a 'title'.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val attrs: Map<String, Any>
    get() {
        return attributes.toMap()
    }

}
