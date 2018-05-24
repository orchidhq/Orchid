package com.eden.orchid.forms.model

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.components.ModularPageList
import com.google.inject.Provider
import javax.inject.Inject

class FormFieldList @Inject
constructor(context: OrchidContext)
    : ModularPageList<FormFieldList, FormField>(context, Provider { getFieldInputTypes(context) }) {

    override fun getItemClass(): Class<FormField> {
        return FormField::class.java
    }

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
