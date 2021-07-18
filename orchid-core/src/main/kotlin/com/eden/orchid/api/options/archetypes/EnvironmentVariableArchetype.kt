package com.eden.orchid.api.options.archetypes

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.EnvironmentVariableAliases
import com.eden.orchid.api.options.annotations.Option

@Description(value = "Loads additional values from secure environment variables.", name = "Environment Variables")
class EnvironmentVariableArchetype : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any> {
        val allEnvironmentVariables = System.getenv() as Map<String, Any>

        val environmentVariableAliases = target
            .javaClass
            .fields
            .filter { it.isAnnotationPresent(EnvironmentVariableAliases::class.java) }
            .flatMap {
                val envAliases = it.getAnnotation(EnvironmentVariableAliases::class.java).value.toList()
                val intersection = allEnvironmentVariables.keys.intersect(envAliases)

                val key: String = if (!EdenUtils.isEmpty(it.getAnnotation(Option::class.java).value)) {
                        it.getAnnotation(Option::class.java).value
                    } else {
                        it.name
                    }

                intersection.map { presentEnvAlias ->
                    key to allEnvironmentVariables[presentEnvAlias]!!
                }
            }
            .toMap()

        return allEnvironmentVariables + environmentVariableAliases
    }
}
