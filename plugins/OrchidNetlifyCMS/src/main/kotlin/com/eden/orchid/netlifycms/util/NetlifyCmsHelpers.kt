package com.eden.orchid.netlifycms.util

import com.eden.orchid.Orchid
import com.eden.orchid.api.options.OptionHolderDescription
import com.eden.orchid.api.options.OptionsDescription
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.theme.components.ComponentHolder
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.OrchidMenu
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.impl.relations.AssetRelation
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import org.json.JSONArray
import org.json.JSONObject
import java.awt.MenuItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun OptionsDescription.toNetlifyCmsField(): JSONObject {
    val field = JSONObject()
    field.put("label", this.key.from { camelCase() }.to { titleCase() })
    field.put("name", this.key)
    field.put("description", this.description)
    field.put("required", false)

    var widget = if (Number::class.java.isAssignableFrom(this.optionType)) {
        "number"
    }
    else if (Boolean::class.java.isAssignableFrom(this.optionType)) {
        "boolean"
    }
    else if (this.optionType.isArray) {
        "list"
    }
    else {
        when (this.optionType) {
            LocalDate::class.java -> "date"
            LocalTime::class.java -> "datetime"
            LocalDateTime::class.java -> "datetime"
            ComponentHolder::class.java -> "list"
            OrchidMenu::class.java -> "list"
            AssetRelation::class.java -> "image"
            String::class.java -> "string"
            else -> "string"
        }
    }

    if(this.optionType == ComponentHolder::class.java) {
        val extractor = Orchid.getInstance().context.injector.getInstance(OptionsExtractor::class.java)
        field.put("fields", extractor.describeAllOptions(OrchidComponent::class.java).getNetlifyCmsFields())
    }
    else if(this.optionType == OrchidComponent::class.java) {
        val typeField = JSONObject()
        typeField.put("label", "Type")
        typeField.put("name", "Type")
        typeField.put("description", "The Component type")
        typeField.put("required", false)
        field.put("type", typeField)
    }
    else if(this.optionType == OrchidMenu::class.java) {
        val extractor = Orchid.getInstance().context.injector.getInstance(OptionsExtractor::class.java)
        field.put("fields", extractor.describeAllOptions(MenuItem::class.java).getNetlifyCmsFields())
    }
    else if(this.optionType == OrchidMenuItem::class.java) {
        val typeField = JSONObject()
        typeField.put("label", "Type")
        typeField.put("name", "Type")
        typeField.put("description", "The MenuItem type")
        typeField.put("required", false)
        field.put("type", typeField)
    }

    field.put("widget", widget)
    return field
}

fun OptionHolderDescription.getNetlifyCmsFields(): JSONArray {
    val fields = JSONArray()

    this.optionsDescriptions.forEach {
        fields.put(it.toNetlifyCmsField())
    }

    val requiredFields = arrayOf("title", "body")

    requiredFields.forEach { fieldName ->
        var hasRequiredField = false
        for(i in 0 until fields.length()) {
            if(fields.getJSONObject(i).getString("name") == fieldName) {
                hasRequiredField = true
                break
            }
        }

        if(!hasRequiredField) {
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
    if(fieldName == "body") {
        field.put("label", "Page Content")
        field.put("widget", "markdown")
    }
    else if(fieldName == "title") {
        field.put("label", "Title")
        field.put("widget", "string")
    }
    return field;
}
