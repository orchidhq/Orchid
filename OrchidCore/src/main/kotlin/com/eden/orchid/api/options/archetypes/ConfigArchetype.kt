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
        var configOptions: Map<String, Any>? = null
        val eventOptions: Map<String, Any>?

        if (!EdenUtils.isEmpty(archetypeKey)) {
            val contextOptions = context.query(archetypeKey)
            if (EdenUtils.elementIsObject(contextOptions)) {
                configOptions = (contextOptions.element as JSONObject).toMap()
            }
        }

        val options = Orchid.Lifecycle.ArchetypeOptions(archetypeKey, target)
        context.broadcast(options)
        eventOptions = options.config

        return EdenUtils.merge(configOptions, eventOptions)
    }

}
