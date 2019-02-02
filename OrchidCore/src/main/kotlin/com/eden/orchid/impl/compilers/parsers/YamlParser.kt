package com.eden.orchid.impl.compilers.parsers

import com.eden.orchid.api.compilers.OrchidParser
import org.yaml.snakeyaml.Yaml
import java.util.HashMap
import javax.inject.Inject

class YamlParser @Inject
constructor() : OrchidParser(100) {

    override fun getDelimiter(): String {
        return "-"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("yml", "yaml")
    }

    @Suppress("UNCHECKED_CAST")
    override fun parse(extension: String, input: String): Map<String, Any>? {
        try {
            val yamlData = Yaml().load<Any>(input)

            if (yamlData is Map<*, *>) {
                return yamlData as Map<String, Any>
            } else if (yamlData is List<*>) {
                val yamlObject = HashMap<String, Any>()
                yamlObject[OrchidParser.arrayAsObjectKey] = yamlData
                return yamlObject
            }
        } catch (e: Exception) {
        }

        return null
    }

    override fun serialize(extension: String, input: Any): String {
        return Yaml().dump(input)
    }
}
