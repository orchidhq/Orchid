package com.eden.orchid.impl.compilers.text

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import java.io.OutputStream
import java.io.OutputStreamWriter

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Archetype(value = ConfigArchetype::class, key = "services.compilers.txt")
class TextCompiler
@Inject
constructor() : OrchidCompiler(800) {

    override fun compile(os: OutputStream, resource: OrchidResource?, extension: String, input: String, data: MutableMap<String, Any>?) {
        OutputStreamWriter(os).append(input).close()
    }

    override fun getOutputExtension(): String {
        return "html"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("txt", "text")
    }
}
