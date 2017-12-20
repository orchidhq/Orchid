package com.eden.orchid.languages.asciidoc

import com.eden.orchid.api.compilers.OrchidCompiler
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AsciiDoctorCompiler @Inject
constructor() : OrchidCompiler(800) {

    private val asciidoctor: Asciidoctor
    private val options: Options

    init {
        asciidoctor = Asciidoctor.Factory.create()
        options = Options()
    }

    override fun compile(extension: String, source: String, data: Map<String, Any>): String {
        return asciidoctor.convert(source, options)
    }

    override fun getOutputExtension(): String {
        return "html"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("ad", "adoc", "asciidoc", "asciidoctor")
    }
}
