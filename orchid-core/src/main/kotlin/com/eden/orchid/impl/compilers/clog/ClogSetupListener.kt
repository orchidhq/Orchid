package com.eden.orchid.impl.compilers.clog

import clog.Clog
import clog.ClogPlatform
import clog.ClogProfile
import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.dsl.addTagToBlacklist
import clog.dsl.tag
import clog.dsl.updateProfile
import clog.impl.DefaultTagProvider
import clog.impl.DelegatingLogger
import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.events.On
import com.eden.orchid.api.events.OrchidEventListener
import com.eden.orchid.utilities.SuppressedWarnings
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.LogRecord
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@JvmSuppressWildcards
@Suppress(SuppressedWarnings.UNUSED_PARAMETER)
class ClogSetupListener
@Inject
constructor(
    private val contextProvider: Provider<OrchidContext>,
    private val templateTagsProvider: Provider<Set<TemplateFunction>>
) : OrchidEventListener {

    private val warningLogger = AbstractLogCollector(contextProvider, "Warnings:", Clog.Priority.WARNING)
    private val errorLogger = AbstractLogCollector(contextProvider, "Errors:", Clog.Priority.ERROR)
    private val fatalLogger = AbstractLogCollector(contextProvider, "Fatal exceptions:", Clog.Priority.FATAL)

    @On(Orchid.Lifecycle.InitComplete::class)
    fun onInitComplete(event: Orchid.Lifecycle.InitComplete) {
        Clog.updateProfile {
            it.copy(
                logger = DelegatingLogger(
                    listOf(
                        DelegatingLogger.FilteredLogger(
                            object : ClogFilter {
                                override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean =
                                    priority < Clog.Priority.WARNING
                            },
                            ClogPlatform.createDefaultLogger()
                        ),
                        DelegatingLogger.FilteredLogger(
                            object : ClogFilter {
                                override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean =
                                    priority == Clog.Priority.WARNING
                            },
                            warningLogger
                        ),
                        DelegatingLogger.FilteredLogger(
                            object : ClogFilter {
                                override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean =
                                    priority == Clog.Priority.ERROR
                            },
                            errorLogger
                        ),
                        DelegatingLogger.FilteredLogger(
                            object : ClogFilter {
                                override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean =
                                    priority == Clog.Priority.FATAL
                            },
                            fatalLogger
                        )
                    )
                )
            )
        }
    }

    @On(Orchid.Lifecycle.BuildFinish::class)
    fun onBuildFinish(event: Orchid.Lifecycle.BuildFinish) {
        printPendingMessages()
    }

    @On(Orchid.Lifecycle.DeployFinish::class)
    fun onBuildFinish(deployFinish: Orchid.Lifecycle.DeployFinish) {
        printPendingMessages()
    }

    @On(Orchid.Lifecycle.Shutdown::class)
    fun onShutdown(event: Orchid.Lifecycle.Shutdown) {
        printPendingMessages()
    }

    private fun printPendingMessages() {
        warningLogger.printAllMessages()
        errorLogger.printAllMessages()
        fatalLogger.printAllMessages()
    }

    companion object {

        @JvmStatic
        fun registerJavaLoggingHandler() {
            // reset() will remove all default handlers
            LogManager.getLogManager().reset()
            val rootLogger = LogManager.getLogManager().getLogger("")

            // add our custom Java Logging handler
            rootLogger.addHandler(ClogJavaLoggingHandler())

            // ignore annoying jsass messages
            Clog.addTagToBlacklist("io.bit3.jsass.adapter.NativeLoader")

            // Ignore Pebble internal logging
            Clog.addTagToBlacklist("com.mitchellbosecke.pebble.lexer.LexerImpl")
            Clog.addTagToBlacklist("com.mitchellbosecke.pebble.lexer.TemplateSource")
            Clog.addTagToBlacklist("com.mitchellbosecke.pebble.PebbleEngine")

            // OpenHtmlToPdf
            Clog.addTagToBlacklist("org.apache.pdfbox.pdmodel.font.FileSystemFontProvider")
        }
    }
}

data class LogMessage(val message: String, val throwable: Throwable?)

private class AbstractLogCollector(
    val contextProvider: Provider<OrchidContext>,
    val headerMessage: String,
    val loggerPriority: Clog.Priority
) : ClogLogger {

    private val messages: MutableMap<String?, MutableSet<LogMessage>> = HashMap()

    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        messages.getOrPut(tag) { HashSet() }.add(LogMessage(message, null))
        if (!contextProvider.get().state.isWorkingState) {
            printAllMessages()
        }
    }

    override fun logException(priority: Clog.Priority, tag: String?, throwable: Throwable) {
        messages.getOrPut(tag) { HashSet() }.add(LogMessage("", throwable))
        if (!contextProvider.get().state.isWorkingState) {
            printAllMessages()
        }
    }

    fun printAllMessages() {
        if (messages.isNotEmpty()) {
            ClogProfile(tagProvider = DefaultTagProvider("")).log(headerMessage)

            messages.forEach { tag, logMessages ->
                val logger = ClogProfile(tagProvider = DefaultTagProvider(tag))
                logger.log("")
                logMessages.forEach { message ->
                    if (message.throwable != null) {
                        logger.log("    - " + message.message, message.throwable)
                    } else {
                        logger.log("    - " + message.message)
                    }
                }
                println("")
            }

            messages.clear()
            println("")
        }
    }
}

private class ClogJavaLoggingHandler : Handler() {

    override fun publish(record: LogRecord) {
        val level = record.level
        val levelInt = level.intValue()

        val clog = Clog.tag(record.sourceClassName)

        if (level == Level.OFF) {
            // do nothing
        } else if (level == Level.ALL) {
            // always log at verbose level
            clog.v(record.message)
        } else {
            // log at closest Clog level
            if (levelInt >= Level.SEVERE.intValue()) {
                clog.e(record.message)
            } else if (levelInt >= Level.WARNING.intValue()) {
                clog.w(record.message)
            } else if (levelInt >= Level.INFO.intValue()) {
                clog.i(record.message)
            } else if (levelInt >= Level.CONFIG.intValue()) {
                clog.d(record.message)
            } else {
                clog.v(record.message)
            }
        }
    }

    override fun flush() {}

    @Throws(SecurityException::class)
    override fun close() {
    }
}
