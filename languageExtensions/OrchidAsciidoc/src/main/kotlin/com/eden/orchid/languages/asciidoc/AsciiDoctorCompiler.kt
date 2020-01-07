package com.eden.orchid.languages.asciidoc

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.compilers.OrchidCompiler
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import org.asciidoctor.SafeMode
import org.asciidoctor.log.LogHandler
import org.asciidoctor.log.LogRecord
import org.asciidoctor.log.Severity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AsciiDoctorCompiler
@Inject
constructor() : OrchidCompiler(800), LogHandler {

    private val asciidoctor: Asciidoctor = Asciidoctor.Factory.create()
    private val options: Options = Options()

    init {
        asciidoctor.registerLogHandler(this)
    }

    override fun compile(extension: String, source: String, data: Map<String, Any>): String {
        val safeMode  = data.getOrDefault("asciiDocSafeMode", "SECURE") as String
        options.setSafe(SafeMode.valueOf(safeMode))
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
            Severity.DEBUG   -> Clog.d(logRecord.message)
            Severity.INFO    -> Clog.i(logRecord.message)
            Severity.WARN    -> Clog.w(logRecord.message)
            Severity.ERROR   -> Clog.e(logRecord.message)
            Severity.FATAL   -> Clog.e(logRecord.message)
            Severity.UNKNOWN -> Clog.d(logRecord.message)
            else             -> Clog.d(logRecord.message)
        }
    }
}
