package com.eden.orchid.forms

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.options.converters.FlexibleIterableConverter
import com.eden.orchid.api.options.converters.FlexibleMapConverter
import com.eden.orchid.forms.model.FormFieldList
import com.google.inject.Provider
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Field
import javax.inject.Inject

class FormFieldListOptionExtractor @Inject
constructor(private val contextProvider: Provider<OrchidContext>, private val iterableConverter: FlexibleIterableConverter, private val mapConverter: FlexibleMapConverter) : OptionExtractor<FormFieldList>(100) {

    override fun acceptsClass(clazz: Class<*>): Boolean {
        return clazz == FormFieldList::class.java
    }

    override fun getOption(field: Field, sourceObject: Any, key: String): FormFieldList? {
        val iterable = iterableConverter.convert(sourceObject, "key").second

        val jsonArray = JSONArray()
        for (o in iterable) {
            val map = mapConverter.convert(o).second
            jsonArray.put(JSONObject(map))
        }

        return if (jsonArray.length() > 0) {
            FormFieldList(contextProvider.get(), jsonArray)
        }
        else null
    }

    override fun getDefaultValue(field: Field): FormFieldList {
        return FormFieldList(contextProvider.get(), JSONArray())
    }

    override fun describeDefaultValue(field: Field): String {
        return "Empty FormFieldList"
    }

}
