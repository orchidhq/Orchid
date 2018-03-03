package com.eden.orchid.presentations

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.presentations.model.Presentation
import com.eden.orchid.presentations.model.PresentationsModel
import com.google.inject.Provider
import org.json.JSONObject
import java.lang.reflect.Field
import javax.inject.Inject

class PresentationOptionExtractor @Inject
constructor(private val contextProvider: Provider<OrchidContext>, private val converter: StringConverter, val model: PresentationsModel) : OptionExtractor<Presentation>(1000) {

    override fun acceptsClass(clazz: Class<*>): Boolean {
        return clazz == Presentation::class.java
    }

    override fun getOption(field: Field, options: JSONObject, key: String): Presentation? {
        var toReturn: Presentation? = null
        if (options.has(key)) {

            val value = converter.convert(options.get(key))
            if (value.first) {
                toReturn = model.presentations.getOrDefault(value.second, null)
            }
        }

        if(toReturn == null) {
            toReturn = getDefaultValue(field)
        }

        if(toReturn == null) {
            Clog.w("Presentation with key '${options.get(key)}' does not exist")
        }

        return toReturn
    }

    override fun getDefaultValue(field: Field): Presentation? {
        return null
    }

    override fun getList(field: Field, options: JSONObject, key: String): List<Presentation>? {
        return null
    }

    override fun getArray(field: Field, options: JSONObject, key: String): Array<Any>? {
        return null
    }
}
