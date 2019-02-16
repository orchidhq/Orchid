package com.eden.orchid.groovydoc.helpers

import com.caseyjbrooks.clog.Clog
import com.copperleaf.groovydoc.json.GroovydocInvoker
import com.copperleaf.groovydoc.json.GroovydocInvokerImpl
import com.copperleaf.groovydoc.json.models.GroovydocRootdoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.utilities.InputStreamIgnorer
import com.eden.orchid.utilities.OrchidUtils
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Named

class OrchidGroovydocInvokerImpl
@Inject
constructor(
    @Named("src") val resourcesDir: String,
    val context: OrchidContext
) : OrchidGroovydocInvoker {

    var hasRunGroovydoc = false
    val cacheDir: Path by lazy { OrchidUtils.getCacheDir("groovydoc") }
    val outputDir: Path by lazy { OrchidUtils.getTempDir("groovydoc", true) }
    val groovydocRunner: GroovydocInvoker by lazy {
        GroovydocInvokerImpl(cacheDir)
    }

    override fun getRootDoc(
        sourceDirs: List<String>,
        extraArgs: List<String>
    ): GroovydocRootdoc? {
        if(hasRunGroovydoc) {
            val cachedRootDoc = groovydocRunner.loadCachedRootDoc(outputDir)
            if (cachedRootDoc != null) {
                Clog.i("returning cached groovydoc")
                return cachedRootDoc
            }
        }

        val sources = sourceDirs.map { File(resourcesDir).toPath().resolve(it) }
        val rootDoc = groovydocRunner.getRootDoc(sources, outputDir, extraArgs) { inputStream ->
            InputStreamIgnorer(inputStream)
        }
        hasRunGroovydoc = true
        return rootDoc
    }

}