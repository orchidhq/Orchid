package com.eden.orchid.utilities

import com.caseyjbrooks.clog.Clog
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class InputStreamCollector(private val inputStream: InputStream) : Runnable {

    var output = StringBuffer()

    override fun run() {
        output = StringBuffer()
        BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().forEach { output.append(it + "\n") }
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
        val transform: ((String)->String)? = null
) : Runnable {

    override fun run() {
        BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().forEach {
            val actualMessage = transform?.invoke(it) ?: it
            if (tag != null) {
                Clog.tag(tag).log(actualMessage)
            }
            else {
                Clog.noTag().log(actualMessage)
            }
        }
    }
}

class InputStreamIgnorer(private val inputStream: InputStream) : Runnable {

    override fun run() {
        BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().forEach {
            // do nothing with it
        }
    }
}

fun convertOutputStream(writer: (OutputStream) -> Unit): InputStream {
    val pipedInputStreamPrinter = PipedInputStream()
    val pipedOutputStream = PipedOutputStream(pipedInputStreamPrinter)
    Thread {
        pipedOutputStream.use(writer)
    }.start()

    return pipedInputStreamPrinter
}
