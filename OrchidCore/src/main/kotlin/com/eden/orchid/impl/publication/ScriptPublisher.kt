package com.eden.orchid.impl.publication

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.common.util.IOStreamUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.publication.OrchidPublisher
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named
import javax.validation.constraints.NotEmpty

@Description(value = "Run arbitrary shell scripts.", name = "Script")
class ScriptPublisher
@Inject
constructor(
    @Named("src") private val resourcesDir: String
) : OrchidPublisher("script") {

    @Option
    @Description("The executable name")
    @NotEmpty(message = "Must set the command to run.")
    lateinit var command: List<String>

    @Option
    @Description("The working directory of the script to run")
    lateinit var cwd: String

    override fun publish(context: OrchidContext) {
        val builder = ProcessBuilder()
        builder.command(*command.toTypedArray())
        var directory: String
        if (!EdenUtils.isEmpty(cwd)) {
            directory = cwd
            if (directory.startsWith("~")) {
                directory = System.getProperty("user.home") + directory.substring(1)
            }
        } else {
            directory = resourcesDir
        }
        builder.directory(File(directory))
        Clog.i("[{}]> {}", directory, command.joinToString(" "))

        val process = builder.start()
        Executors.newSingleThreadExecutor().submit(IOStreamUtils.InputStreamPrinter(process.inputStream, "Script Publisher"))
        process.waitFor()
    }
}
