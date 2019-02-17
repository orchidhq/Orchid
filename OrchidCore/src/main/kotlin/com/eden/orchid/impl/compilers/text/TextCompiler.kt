package com.eden.orchid.impl.compilers.text

import com.eden.orchid.api.compilers.OrchidCompiler

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextCompiler
@Inject
constructor() : OrchidCompiler(800) {

    override fun compile(extension: String, source: String, data: Map<String, Any>): String {
        return source
    }

    override fun getOutputExtension(): String {
        return "html"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("txt", "text")
    }
}
