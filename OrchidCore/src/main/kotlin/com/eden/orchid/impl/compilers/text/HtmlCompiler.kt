package com.eden.orchid.impl.compilers.text

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Archetype(value = ConfigArchetype::class, key = "services.compilers.html")
class HtmlCompiler
@Inject
constructor() : OrchidCompiler(800) {

    override fun compile(extension: String, source: String, data: Map<String, Any>): String {
        return source
    }

    override fun getOutputExtension(): String {
        return "html"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("html", "htm")
    }
}
