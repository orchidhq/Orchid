package com.eden.orchid.api.options.archetypes

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.Theme
import javax.inject.Inject

@Description(
    value = "Configure this item with additional options merged in from `config.yml`, from the object at the " +
            "archetype key, and also from the theme's key.",
    name = "Theme Config"
)
class ThemeConfigArchetype
@Inject
constructor(
    private val context: OrchidContext
) : OptionArchetype {

    @Option
    @BooleanDefault(false)
    var isolated: Boolean = false

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any>? {
        if(target !is Theme) return null

        if(isolated) {
            return null
        }

        val configArchetype = ConfigArchetype(context)

        val baseThemeOptions = configArchetype.getOptions(target, archetypeKey)
        val themeTypeOptions = configArchetype.getOptions(target, target.key)

        return EdenUtils.merge(baseThemeOptions, themeTypeOptions)
    }
}
