package com.eden.orchid.utilities

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.tasks.TaskService
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

class InputStreamCollector(private val inputStream: InputStream) : Runnable {

    var output = StringBuffer()

    override fun run() {
        output = StringBuffer()
        inputStream
            .bufferedReader()
            .lineSequence()
            .forEach { output.appendln(it) }
    }

    override fun toString(): String {
        return output.toString()
    }
}

class InputStreamPrinter
@JvmOverloads
constructor(
    private val inputStream: InputStream,
    val tag: String? = null,
    val transform: ((String) -> String)? = null
) : Runnable {

    override fun run() {
        inputStream
            .bufferedReader()
            .lineSequence()
            .forEach {
                val actualMessage = transform?.invoke(it) ?: it
                if (tag != null) {
                    Clog.tag(tag).log(actualMessage)
                } else {
                    Clog.noTag().log(actualMessage)
                }
            }
    }
}

class InputStreamIgnorer(private val inputStream: InputStream) : Runnable {
    override fun run() {
        inputStream
            .bufferedReader()
            .lineSequence()
            .forEach { /* do nothing with it */ }
    }
}

fun convertOutputStream(context: OrchidContext, writer: (OutputStream) -> Unit): InputStream {
    if (context.taskType == TaskService.TaskType.SERVE) {
        // in serve mode, we get errors trying to convert the streams using pipes, because the server stream is closed
        // before the thread can finish. So we need to do it all in memory.
        //
        // TODO: look into a coroutine-based small-buffered solution to write/read bytes on the same thread, and avoid
        //      having to load it all into memory at once, and avoid having to spawn separate threads for this, too
        val os = ByteArrayOutputStream()
        writer(os)
        return ByteArrayInputStream(os.toByteArray())
    } else {
        // when not in serve mode, we can safely use pipes for copying streams, to conserve
        val pipedInputStreamPrinter = PipedInputStream()
        val pipedOutputStream = PipedOutputStream(pipedInputStreamPrinter)
        Thread {
            pipedOutputStream.use(writer)
        }.start()

        return pipedInputStreamPrinter
    }
}
