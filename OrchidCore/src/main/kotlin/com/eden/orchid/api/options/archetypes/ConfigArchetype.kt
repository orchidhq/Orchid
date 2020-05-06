package com.eden.orchid.api.options.archetypes

import com.eden.common.util.EdenUtils
import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.api.options.annotations.Description
import org.json.JSONObject
import javax.inject.Inject

@Description(
    value = "Configure this item with additional options merged in from `config.yml`, from the object at " + "the archetype key. Dots in the key indicate sub-objects within the site config.",
    name = "Site Config"
)
class ConfigArchetype
@Inject
constructor(
    private val context: OrchidContext
) : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any> {
        return archetypeKey
            .takeIf { it.isNotBlank() }
            ?.let { context.query(it) }
            ?.takeIf { EdenUtils.elementIsObject(it) }
            ?.let { (it.element as JSONObject).toMap() }
            ?: emptyMap()
    }
}
