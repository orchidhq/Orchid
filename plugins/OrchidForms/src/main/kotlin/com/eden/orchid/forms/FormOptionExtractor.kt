package com.eden.orchid.forms

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.forms.model.Form
import com.eden.orchid.forms.model.FormsModel
import com.google.inject.Provider
import org.json.JSONObject
import java.lang.reflect.Field
import javax.inject.Inject

class FormOptionExtractor @Inject
constructor(private val contextProvider: Provider<OrchidContext>, private val converter: StringConverter, private val formsModel: FormsModel) : OptionExtractor<Form>(1000) {

    override fun acceptsClass(clazz: Class<*>): Boolean {
        return clazz == Form::class.java
    }

    override fun getOption(field: Field, options: JSONObject, key: String): Form? {
        if (options.has(key)) {
            if (options.get(key) is JSONObject) {
                return Form(contextProvider.get(), "", options.getJSONObject(key))
            } else {
                val value = converter.convert(options.get(key))
                if (value.first) {
                    return formsModel.forms.getOrDefault(value.second, null)
                }
            }
        }

        return getDefaultValue(field)
    }

    override fun getDefaultValue(field: Field): Form? {
        return null
    }

    override fun getList(field: Field, options: JSONObject, key: String): List<Form>? {
        return null
    }

    override fun getArray(field: Field, options: JSONObject, key: String): Array<Any>? {
        return null
    }
}
