package com.eden.orchid.utilities

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.tasks.TaskService
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

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
