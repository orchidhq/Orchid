package com.eden.orchid.languages.asciidoc

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.languages.asciidoc.extensions.AsciidocIncludeProcessor
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import org.asciidoctor.SafeMode
import org.asciidoctor.log.LogHandler
import org.asciidoctor.log.LogRecord
import org.asciidoctor.log.Severity
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
    private lateinit var options: Options

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

    // configure parsing options from options, before each build phase
    override fun onPostExtraction() {
        options = Options()
        options.setSafe(safeMode)
        // files can be included relative to the default resources directory
        options.setBaseDir(resourcesDir)
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
