package com.eden.orchid.languages.ink

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.Orchid
import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.events.On
import com.eden.orchid.api.events.OrchidEventListener
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.InputStreamIgnorer
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.OutputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InkCompiler : OrchidCompiler(800), OrchidEventListener {

    override fun getSourceExtensions() = arrayOf("ink")
    override fun getOutputExtension() = "json"

    private val binariesCacheDir: File by lazy {
        OrchidUtils.getCacheDir("ink").toAbsolutePath().toFile()
    }

    override fun compile(
        os: OutputStream,
        resource: OrchidResource?,
        extension: String,
        input: String,
        data: MutableMap<String, Any>?
    ) {
        try {
            compileInkSource(os, input)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun compileInkSource(os: OutputStream, input: String) {
        // Write the source text to a temporary file, so we can pass it to the Ink binary
        val tempDir = OrchidUtils.getTempDir("ink", true).toAbsolutePath().toFile().absolutePath
        val tempFile = File(tempDir, "temp.ink")

        tempFile.writeText(input)

        // The Ink binary CLI command is different on each OS
        val baseCommand = when {
            OrchidUtils.isMac -> listOf("./inklecate")
            OrchidUtils.isWindows -> listOf("cmd", "/c", "inklecate.exe")
            OrchidUtils.isLinux -> listOf("mono", "inklecate.exe")

            else -> {
                Clog.e("Ink is not supported on your OS.")
                return
            }
        }.toTypedArray()

        // Run the Ink binary CLI, pointing it to the temporary file we just created
        val process = ProcessBuilder()
            .command(*baseCommand, tempFile.absolutePath)
            .redirectErrorStream(true)
            .directory(binariesCacheDir)
            .start()

        val streamHandler = InputStreamIgnorer(process.inputStream)
        val executor = Executors.newSingleThreadExecutor()
        executor.submit(streamHandler)
        process.waitFor()
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)

        // Locate the generated JSON file that is our compiled story
        val outputFile = tempFile.resolveSibling(tempFile.name + ".json")

        // pipe the output to the OutputStream
        val writer = os.bufferedWriter()
        writer.write(outputFile.readText())
        writer.close()

        // clean up, deleting the temp files
        outputFile.delete()
        tempFile.delete()
    }

// Cache Ink binaries on start
//----------------------------------------------------------------------------------------------------------------------

    @On(Orchid.Lifecycle.OnStart::class)
    fun onStart(event: Orchid.Lifecycle.OnStart) {
        cacheCliBinaries()
    }

    private fun cacheCliBinaries() {
        Clog.i("caching Ink binaries")
        val (subdir, fileNames) = if (OrchidUtils.isMac) {
            "ink_cli/mac" to listOf(
                "ink-engine-runtime.dll",
                "ink_compiler.dll",
                "inklecate"
            )
        } else {
            "ink_cli/windows_and_linux" to listOf(
                "ink-engine-runtime.dll",
                "ink-engine-runtime.xml",
                "ink_compiler.dll",
                "inklecate.exe"
            )
        }

        fileNames.forEach {
            val cachedFile = File(binariesCacheDir, it)
            if(!cachedFile.exists()) {
                FileUtils.copyURLToFile(InkCompiler::class.java.getResource("/$subdir/$it"), cachedFile)
            }
            if(!cachedFile.canExecute()) {
                cachedFile.setExecutable(true)
            }
        }
    }
}
