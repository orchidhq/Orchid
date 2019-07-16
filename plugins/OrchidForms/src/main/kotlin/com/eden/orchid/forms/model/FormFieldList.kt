package com.eden.orchid.forms.model

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.components.ModularPageList
import javax.inject.Provider
import javax.inject.Inject

class FormFieldList
@Inject
constructor(
        context: OrchidContext
) : ModularPageList<FormFieldList, FormField>(context, Provider { getFieldInputTypes(context) }) {

    private var extraFields = mutableMapOf<String, Boolean>()

    override fun getItemClass(): Class<FormField> {
        return FormField::class.java
    }

    fun addExtraField(extraFieldKey: String, fieldName: String, fieldValue: String) {
        if(!extraFields.containsKey(extraFieldKey)) {
            add(mapOf(
                    "type" to "hidden",
                    "key" to fieldName,
                    "value" to fieldValue,
                    "order" to Integer.MAX_VALUE
            ))

            extraFields[extraFieldKey] = true
        }
    }

    companion object {
        fun getFieldInputTypes(context: OrchidContext): Map<String, Class<FormField>> {
            val fieldTypes = HashMap<String, Class<FormField>>()

            val itemTypes = context.resolveSet(FormField::class.java)

            for (itemType in itemTypes) {
                for (itemTypeAtKey in itemType.inputTypes) {
                    fieldTypes[itemTypeAtKey] = itemType.javaClass
                }
            }

            return fieldTypes
        }
    }

}
