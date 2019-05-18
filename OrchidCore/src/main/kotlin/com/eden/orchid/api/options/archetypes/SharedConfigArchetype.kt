package com.eden.orchid.api.options.archetypes

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import org.json.JSONObject
import javax.inject.Inject

@Description(
    value = "Configure this item with additional options merged in from `config.yml`. The configuration object is dynamically loaded from the `from` key of the source data",
    name = "Shared Config"
)
class SharedConfigArchetype
@Inject
constructor(
    private val context: OrchidContext
) : OptionArchetype {

    @Option
    lateinit var from: String

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any>? {
        if(from.isBlank()) return null

        val contextOptions = context.query(from)
        return if (EdenUtils.elementIsObject(contextOptions)) (contextOptions.element as JSONObject).toMap() else null
    }

}