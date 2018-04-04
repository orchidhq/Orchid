package com.eden.orchid.forms.model

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.components.ModularList
import org.json.JSONArray
import javax.inject.Inject

class FormFieldList @Inject
constructor(context: OrchidContext, itemsJson: JSONArray)
    : ModularList<FormFieldList, FormField>(context, FormField::class.java, getFieldInputTypes(context), itemsJson) {

    companion object {
        fun getFieldInputTypes(context: OrchidContext): Map<String, Class<FormField>> {
            val fieldTypes = HashMap<String, Class<FormField>>()

            val itemTypes = context.resolveSet(FormField::class.java)

            for (itemType in itemTypes) {
                for(itemTypeAtKey in itemType.inputTypes) {
                    fieldTypes[itemTypeAtKey] = itemType.javaClass
                }
            }

            return fieldTypes
        }
    }

}