package com.eden.orchid.api.options.archetypes

import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.api.options.annotations.Description

@Description(value = "Loads additional values from secure environment variables.", name = "Environment Variables")
class EnvironmentVariableArchetype : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any> {
        val vars = System.getenv()
        return vars as Map<String, Any>
    }

}
