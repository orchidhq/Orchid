package com.eden.orchid.netlifycms.util

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionHolderDescription
import com.eden.orchid.api.options.OptionsDescription
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.theme.components.ModularList
import com.eden.orchid.api.theme.components.ModularListItem
import com.eden.orchid.impl.relations.AssetRelation
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun OptionsDescription.toNetlifyCmsField(context: OrchidContext, recursionDepth: Int): JSONObject {
    val extractor = context.resolve(OptionsExtractor::class.java)

    val field = JSONObject()
    field.put("label", this.key from { camelCase() } to { titleCase() })
    field.put("name", this.key)
    field.put("description", this.description)
    field.put("required", false)
    field.put("widget", getWidgetType())

    if (ModularList::class.java.isAssignableFrom(this.optionType)) {
        val itemTypesListClass = getModularListItemClass(this.optionType)

        if (itemTypesListClass != null) {
            val itemTypes = context.resolveSet(itemTypesListClass)

            val typeField = JSONObject()
            typeField.put("label", this.javaClass.simpleName)
            typeField.put("name", this.key)
            typeField.put("description", this.description)

            field.put("typeKey", "type")
            field.put(
                    "types",
                    itemTypes
                            .map {
                                val options = extractor.describeAllOptions(it::class.java)

                                val itemType = JSONObject()
                                itemType.put("label", options.descriptiveName)
                                itemType.put("name", (it as ModularListItem<*, *>).getType())
                                itemType.put("description", options.classDescription)
                                itemType.put("widget", "object")

                                val fields = JSONArray()

                                options.optionsDescriptions.forEach { option ->
                                    if (recursionDepth > 0) {
                                        fields.put(option.toNetlifyCmsField(context, recursionDepth - 1))
                                    }
                                }

                                itemType.put("fields", fields)

                                itemType
                            }
            )
        }
    }
    else if (OptionsHolder::class.java.isAssignableFrom(this.optionType)) {
        val options = extractor.describeAllOptions(this.optionType)

        val fields = JSONArray()
        options.optionsDescriptions.forEach { option ->
            if (recursionDepth > 0) {
                fields.put(option.toNetlifyCmsField(context, recursionDepth - 1))
            }
        }

        field.put("fields", fields)
    }
    else if (List::class.java.isAssignableFrom(this.optionType) && this.optionTypeParameters.first() != String::class.java) {
        val options = extractor.describeAllOptions(this.optionTypeParameters.first())

        val fields = JSONArray()
        options.optionsDescriptions.forEach { option ->
            if (recursionDepth > 0) {
                fields.put(option.toNetlifyCmsField(context, recursionDepth - 1))
            }
        }

        field.put("fields", fields)
    }
    else if (this.optionType.isArray && this.optionType.componentType != String::class.java) {
        val options = extractor.describeAllOptions(this.optionType.componentType)

        val fields = JSONArray()
        options.optionsDescriptions.forEach { option ->
            if (recursionDepth > 0) {
                fields.put(option.toNetlifyCmsField(context, recursionDepth - 1))
            }
        }

        field.put("fields", fields)
    }

    return field
}

fun OptionHolderDescription.getNetlifyCmsFields(context: OrchidContext, recursionDepth: Int): JSONArray {
    val fields = JSONArray()

    this.optionsDescriptions.forEach {
        if (recursionDepth > 0) {
            fields.put(it.toNetlifyCmsField(context, recursionDepth - 1))
        }
    }

    val requiredFields = arrayOf("title", "body")

    requiredFields.forEach { fieldName ->
        var hasRequiredField = false
        for (i in 0 until fields.length()) {
            if (fields.getJSONObject(i).getString("name") == fieldName) {
                hasRequiredField = true
                break
            }
        }

        if (!hasRequiredField) {
            fields.put(getRequiredField(fieldName))
        }
    }

    return fields
}

fun String.toNetlifyCmsSlug(): String {
    return this.replace("\\{(.*?)\\}".toRegex(), "{{$1}}")
}

private fun getRequiredField(fieldName: String): JSONObject {
    val field = JSONObject()
    field.put("name", fieldName)
    field.put("widget", "string")
    if (fieldName == "body") {
        field.put("label", "Page Content")
        field.put("widget", "markdown")
    }
    else if (fieldName == "title") {
        field.put("label", "Title")
        field.put("widget", "string")
    }
    return field;
}

private fun OptionsDescription.getWidgetType(): String {
    return when {
        Number::class.java.isAssignableFrom(this.optionType)        -> "number"
        Boolean::class.java.isAssignableFrom(this.optionType)       -> "boolean"
        ModularList::class.java.isAssignableFrom(this.optionType)   -> "list"
        OptionsHolder::class.java.isAssignableFrom(this.optionType) -> "object"
        List::class.java.isAssignableFrom(this.optionType)          -> "list"
        this.optionType.isArray                                     -> "list"
        this.optionType == LocalDate::class.java                    -> "date"
        this.optionType == LocalTime::class.java                    -> "datetime"
        this.optionType == LocalDateTime::class.java                -> "datetime"
        this.optionType == AssetRelation::class.java                -> "image"
        this.optionType == String::class.java                       -> "string"
        else                                                        -> "string"
    }
}

fun getModularListItemClass(modularListClass: Class<*>): Class<*>? {
    for (typeArg in (modularListClass.genericSuperclass as ParameterizedType).actualTypeArguments) {
        val className = typeArg.typeName
        val clazz = Class.forName(className)

        if (ModularListItem::class.java.isAssignableFrom(clazz)) {
            return clazz
        }
    }

    return null
}
