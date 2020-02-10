package com.eden.orchid.impl.compilers.markdown

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.IRender
import com.vladsch.flexmark.util.builder.Extension
import com.vladsch.flexmark.util.data.MutableDataSet
import java.io.OutputStream
import java.io.OutputStreamWriter

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@JvmSuppressWildcards
@Archetype(value = ConfigArchetype::class, key = "services.compilers.md")
class MarkdownCompiler
@Inject
constructor(
    extensionSet: Set<Extension>,
    injectedOptions: Set<MutableDataSet>
) : OrchidCompiler(900) {

    private val parser: Parser
    private val renderer: IRender

    init {

        val options = MutableDataSet()
        options.set(HtmlRenderer.GENERATE_HEADER_ID, true)
        options.set(HtmlRenderer.RENDER_HEADER_ID, true)
        options.set(Parser.HTML_BLOCK_DEEP_PARSE_BLANK_LINE_INTERRUPTS, false)
        options.set(Parser.HTML_BLOCK_DEEP_PARSER, true)


        options.set(Parser.EXTENSIONS, extensionSet)

        for (injectedOption in injectedOptions) {
            options.setAll(injectedOption)
        }

        parser = Parser.builder(options).build()
        renderer = HtmlRenderer.builder(options).build()
    }

    override fun compile(os: OutputStream, resource: OrchidResource?, extension: String, input: String, data: MutableMap<String, Any>?) {
        val writer = OutputStreamWriter(os)
        renderer.render(parser.parse(input), writer)
        writer.close()
    }

    override fun getOutputExtension(): String {
        return "html"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("md", "markdown")
    }
}
