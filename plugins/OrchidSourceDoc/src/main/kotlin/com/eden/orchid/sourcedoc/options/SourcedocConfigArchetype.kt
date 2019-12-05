package com.eden.orchid.sourcedoc.options

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.sourcedoc.model.SourceDocModuleConfig
import com.eden.orchid.sourcedoc.page.SourceDocModuleHomePage
import com.eden.orchid.sourcedoc.page.SourceDocPage
import javax.inject.Inject

@Description(
    value = "Configure this item with additional options merged in from `config.yml`, from the object at " + "the archetype key. Dots in the key indicate sub-objects within the site config.",
    name = "Site Config"
)
open class SourcedocPageConfigArchetype
@Inject
constructor(
    protected val context: OrchidContext
) : OptionArchetype {

    private val configArchetype = ConfigArchetype(context)

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any> {
        val actualArchetypeKeys = mutableListOf<String>()

        when (target) {
            is SourceDocModuleHomePage -> {
                actualArchetypeKeys.add("sourcedoc.$archetypeKey")
                actualArchetypeKeys.add("sourcedoc.moduleHome${archetypeKey.capitalize()}")

                actualArchetypeKeys.add("${target.moduleType}.$archetypeKey")
                actualArchetypeKeys.add("${target.moduleType}.moduleHome${archetypeKey.capitalize()}")
                if (target.moduleGroup.isNotBlank()) {
                    actualArchetypeKeys.add("${target.moduleType}.${target.moduleGroup}${archetypeKey.capitalize()}")
                    actualArchetypeKeys.add("${target.moduleType}.${target.moduleGroup}ModuleHome${archetypeKey.capitalize()}")
                }
            }
            is SourceDocPage<*> -> {
                actualArchetypeKeys.add("sourcedoc.$archetypeKey")
                actualArchetypeKeys.add("sourcedoc.source${archetypeKey.capitalize()}")

                actualArchetypeKeys.add("${target.moduleType}.$archetypeKey")
                actualArchetypeKeys.add("${target.moduleType}.source${archetypeKey.capitalize()}")
                actualArchetypeKeys.add("${target.moduleType}.${target.key}${archetypeKey.capitalize()}")

                if (target.moduleGroup.isNotBlank()) {
                    actualArchetypeKeys.add("${target.moduleType}.${target.moduleGroup}${archetypeKey.capitalize()}")
                    actualArchetypeKeys.add("${target.moduleType}.${target.moduleGroup}Source${archetypeKey.capitalize()}")
                    actualArchetypeKeys.add("${target.moduleType}.${target.moduleGroup}${target.key.capitalize()}${archetypeKey.capitalize()}")
                }
            }
            is SourceDocModuleConfig -> {
                actualArchetypeKeys.add("sourcedoc")
                actualArchetypeKeys.add("sourcedoc.$archetypeKey")
                actualArchetypeKeys.add(target.moduleType)
                actualArchetypeKeys.add("${target.moduleType}.$archetypeKey")
            }
            else -> throw IllegalArgumentException("target of SourcedocPageConfigArchetype must be a Sourcedoc page or module")
        }

        return EdenUtils.merge(*actualArchetypeKeys.map { configArchetype.getOptions(target, it) }.toTypedArray())
    }
}
