package com.eden.orchid.utilities

import com.caseyjbrooks.clog.Clog
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class InputStreamCollector(private val inputStream: InputStream) : Runnable {

    var output = StringBuffer()

    override fun run() {
        output = StringBuffer()
        BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8"))).lines().forEach { output.append(it + "\n") }
    }

    override fun toString(): String {
        return output.toString()
    }
}

class InputStreamPrinter

@JvmOverloads
constructor(private val inputStream: InputStream, val tag: String? = null) : Runnable {

    override fun run() {
        BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8"))).lines().forEach {
            if(tag != null) {
                Clog.tag(tag).log(it)
            }
            else {
                Clog.noTag().log(it)
            }
        }
    }
}

class InputStreamIgnorer(private val inputStream: InputStream) : Runnable {

    override fun run() {
        BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8"))).lines().forEach {
            // do nothing with it
        }
    }

}