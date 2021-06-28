package com.eden.orchid.languages.asciidoc

import clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.languages.asciidoc.extensions.AsciidocIncludeProcessor
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import org.asciidoctor.SafeMode
import org.asciidoctor.jruby.internal.JRubyAsciidoctor
import org.asciidoctor.log.LogHandler
import org.asciidoctor.log.LogRecord
import org.asciidoctor.log.Severity
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.StringReader
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@Archetype(value = ConfigArchetype::class, key = "services.compilers.adoc")
class AsciiDoctorCompiler
@Inject
constructor(
    @Named("src") private val resourcesDir: String,
    includeProcessor: AsciidocIncludeProcessor
) : OrchidCompiler(800), LogHandler {

    private val asciidoctor: Asciidoctor = Asciidoctor.Factory.create()

    @Option
    @Description("Customize the security level Asciidoctor will run under. One of [UNSAFE, SAFE, SERVER, SECURE]")
    @StringDefault("SAFE")
    lateinit var safeMode: SafeMode

    // register Asciidoctor instance, once during entire Orchid lifetime
    init {
        asciidoctor.registerLogHandler(this)
        asciidoctor.javaExtensionRegistry().apply {
            includeProcessor(includeProcessor)
        }
    }

    override fun compile(os: OutputStream, resource: OrchidResource?, extension: String, input: String, data: MutableMap<String, Any>?) {
        val reader = StringReader(input)
        val writer = OutputStreamWriter(os)

        val options = Options()
        options.setSafe(safeMode)
        // files can be included relative to the default resources directory
        if(resource is FileResource) {
            // file resources set their base dir to the file's own base dir, so relative includes are resolved properly
            options.setBaseDir(resource.file.absoluteFile.parentFile.absolutePath)
        }
        else {
            // otherwise, use the default resources dir
            options.setBaseDir(resourcesDir)
        }

        asciidoctor.convert(reader, writer, options)

        writer.close()
    }

    override fun getOutputExtension(): String {
        return "html"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("ad", "adoc", "asciidoc", "asciidoctor")
    }

    override fun log(logRecord: LogRecord?) {
        if (logRecord == null) return

        when (logRecord.severity) {
            Severity.DEBUG -> Clog.d(logRecord.message)
            Severity.INFO -> Clog.i(logRecord.message)
            Severity.WARN -> Clog.w(logRecord.message)
            Severity.ERROR -> Clog.e(logRecord.message)
            Severity.FATAL -> Clog.e(logRecord.message)
            Severity.UNKNOWN -> Clog.d(logRecord.message)
            else -> Clog.d(logRecord.message)
        }
    }
}
