package com.eden.orchid.netlifycms.util

import com.eden.orchid.api.options.OptionsDescription
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun OptionsDescription.toNetlifyCmsField(): JSONObject {
    val field = JSONObject()
    field.put("label", this.key)
    field.put("name", this.key)
    field.put("description", this.description)

    var widget = if (Number::class.java.isAssignableFrom(this.optionType)) {
        "number"
    }
    else if (Boolean::class.java.isAssignableFrom(this.optionType)) {
        "boolean"
    }
    else {
        when (this.optionType) {
            LocalDate::class.java -> "date"
            LocalTime::class.java -> "datetime"
            LocalDateTime::class.java -> "datetime"
            else -> "text"
        }
    }
    field.put("widget", widget)
    return field
}
