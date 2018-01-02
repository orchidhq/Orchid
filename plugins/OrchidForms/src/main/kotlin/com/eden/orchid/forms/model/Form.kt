package com.eden.orchid.forms.model

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import org.json.JSONObject


class Form(protected val context: OrchidContext, val key: String, val formData: JSONObject) : OptionsHolder {

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
    lateinit var attributesMap: JSONObject

    var fields: MutableMap<String, FormField> = LinkedHashMap()

    init {
        try {
            extractOptions(context, formData)

            if (formData.has("fields")) {
                val formFields = formData.getJSONObject("fields")

                val fieldTypes = context.resolveSet(FormField::class.java)
                val tmpFields = HashMap<String, FormField>()
                val tmpFieldOrder = ArrayList<Pair<String, Int>>()

                for (fieldKey in formFields.keySet()) {
                    for (fieldType in fieldTypes) {
                        val fieldConfig = formFields.getJSONObject(fieldKey)
                        if (fieldType.acceptsType(fieldConfig.getString("type"))) {
                            val formField = context.injector.getInstance(fieldType.javaClass)
                            formField.initialize(fieldKey, fieldConfig)
                            tmpFields.put(fieldKey, formField)
                            tmpFieldOrder.add(fieldKey to formField.order)
                        }
                    }
                }

                tmpFieldOrder.sortBy { it.second }
                tmpFieldOrder.forEach {
                    fields.put(it.first, tmpFields[it.first]!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAttributes(): String {
        var attrs = ""

        for (key in attributesMap.keySet()) {
            attrs += key + "=\"" + attributesMap.get(key).toString() + "\" "
        }

        return attrs
    }

}
