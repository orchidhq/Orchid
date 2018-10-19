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

class FormOptionExtractor
@Inject
constructor(
        private val contextProvider: Provider<OrchidContext>,
        private val converter: StringConverter,
        private val formsModel: FormsModel
) : OptionExtractor<Form>(1000) {

    override fun acceptsClass(clazz: Class<*>): Boolean {
        return clazz == Form::class.java
    }

    override fun getOption(field: Field, sourceObject: Any, key: String): Form? {
        if (sourceObject is JSONObject) {
            return Form(contextProvider.get(), "", sourceObject.toMap())
        }
        else {
            val value = converter.convert(String::class.java, sourceObject)
            if (value.first) {
                return formsModel.forms.getOrDefault(value.second, null)
            }
        }

        return null
    }

    override fun getDefaultValue(field: Field): Form? {
        return null
    }

}
