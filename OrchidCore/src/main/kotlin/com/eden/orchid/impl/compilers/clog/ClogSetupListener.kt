package com.eden.orchid.impl.compilers.clog

import com.caseyjbrooks.clog.Clog
import com.caseyjbrooks.clog.ClogLogger
import com.caseyjbrooks.clog.DefaultLogger
import com.caseyjbrooks.clog.parseltongue.Parseltongue
import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.events.On
import com.eden.orchid.api.events.OrchidEventListener
import com.eden.orchid.utilities.SuppressedWarnings
import javax.inject.Provider
import java.util.Arrays
import java.util.HashMap
import java.util.HashSet
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.LogRecord
import javax.inject.Inject
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

    private val warningLogger = AbstractLogCollector("Warnings:", Clog.Priority.WARNING)
    private val errorLogger = AbstractLogCollector("Errors:", Clog.Priority.ERROR)
    private val fatalLogger = AbstractLogCollector("Fatal exceptions:", Clog.Priority.FATAL)

    @On(Orchid.Lifecycle.InitComplete::class)
    fun onInitComplete(event: Orchid.Lifecycle.InitComplete) {
        Clog.getInstance().addLogger(Clog.KEY_W, warningLogger)
        Clog.getInstance().addLogger(Clog.KEY_E, errorLogger)
        Clog.getInstance().addLogger(Clog.KEY_WTF, fatalLogger)
    }

    @On(Orchid.Lifecycle.OnStart::class)
    fun onStart(event: Orchid.Lifecycle.OnStart) {
        val formatter = Clog.getInstance().formatter
        if (formatter is Parseltongue) {
            val incantations = templateTagsProvider.get()
                .map { templateTag ->
                    ClogIncantationWrapper(
                        contextProvider,
                        templateTag.name,
                        Arrays.asList(*templateTag.parameters()),
                        templateTag.javaClass
                    )
                }

            formatter.addSpells(*incantations.toTypedArray())
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
            //reset() will remove all default handlers
            LogManager.getLogManager().reset()
            val rootLogger = LogManager.getLogManager().getLogger("")

            // add our custom Java Logging handler
            rootLogger.addHandler(ClogJavaLoggingHandler())

            // ignore annoying Hibernate Validator and JSass messages
            Clog.getInstance().addTagToBlacklist("org.hibernate.validator.internal.util.Version")
            Clog.getInstance().addTagToBlacklist("io.bit3.jsass.adapter.NativeLoader")
        }
    }

}

data class LogMessage(val message: String, val throwable: Throwable?)

private class AbstractLogCollector(
    val headerMessage: String,
    val loggerPriority: Clog.Priority
) : ClogLogger {

    val messages: MutableMap<String, MutableSet<LogMessage>> = HashMap()

    override fun priority(): Clog.Priority = loggerPriority

    override fun isActive(): Boolean = true

    override fun log(tag: String, message: String): Int {
        messages.getOrPut(tag) { HashSet() }.add(LogMessage(message, null))
        if (!Orchid.getInstance().state.isWorkingState) {
            printAllMessages()
        }

        return 0
    }

    override fun log(tag: String, message: String, throwable: Throwable): Int {
        messages.getOrPut(tag) { HashSet() }.add(LogMessage(message, null))
        if (!Orchid.getInstance().state.isWorkingState) {
            printAllMessages()
        }

        return 0
    }

    fun printAllMessages() {
        if (messages.size > 0) {
            val logger = DefaultLogger(priority())

            logger.log("", headerMessage)

            messages.forEach { tag, logMessages ->
                logger.log(tag, "")
                logMessages.forEach { message ->
                    if (message.throwable != null) {
                        logger.log("", "    - " + message.message, message.throwable)
                    } else {
                        logger.log("", "    - " + message.message)
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