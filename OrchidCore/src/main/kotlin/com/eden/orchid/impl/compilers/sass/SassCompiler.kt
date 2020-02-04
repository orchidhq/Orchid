package com.eden.orchid.impl.compilers.sass

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import io.bit3.jsass.Compiler
import io.bit3.jsass.Options
import io.bit3.jsass.adapter.NativeAdapter
import okhttp3.internal.closeQuietly
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.URI
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Archetype(value = ConfigArchetype::class, key = "services.compilers.scss")
class SassCompiler
@Inject
constructor(
    private val importer: SassImporter
) : OrchidCompiler(800) {

    private val previousContextRegex = Pattern.compile("^//\\s*?CONTEXT=(.*?)$", Pattern.MULTILINE)

    enum class CompilerSyntax(val ext: String) {
        SASS("sass"), SCSS("scss"), UNSPECIFIED("")
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf(CompilerSyntax.SCSS.ext, CompilerSyntax.SASS.ext)
    }

    override fun getOutputExtension(): String {
        return "css"
    }

    override fun compile(os: OutputStream, extension: String, input: String, data: MutableMap<String, Any>?) {
        val options = Options()
        options.importers.add(importer)
        options.setIsIndentedSyntaxSrc(false)

        var sourceContext = ""

        val m = previousContextRegex.matcher(input)
        if (m.find()) {
            sourceContext = m.group(1)
        }

        val actualScssInput = if (extension == CompilerSyntax.SASS.ext) Compiler.sass2scss(input, 0) else input

        val result = try {
            if (EdenUtils.isEmpty(sourceContext)) {
                Compiler().compileString(actualScssInput, options).css
            } else {
                Compiler().compileString(actualScssInput, URI(sourceContext), URI(sourceContext), options).css
            }
        } catch (e: Exception) {
            Clog.e("error compiling .{} content: {}", e, extension, e.message)
            ""
        }

        OutputStreamWriter(os).append(result).close()
    }
}
