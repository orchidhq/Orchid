package com.eden.orchid.javadoc.helpers

import com.copperleaf.javadoc.json.JavadocdocInvoker
import com.copperleaf.javadoc.json.JavadocdocInvokerImpl
import com.copperleaf.javadoc.json.models.JavadocRootdoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.utilities.InputStreamIgnorer
import com.eden.orchid.utilities.OrchidUtils
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Named

class OrchidJavadocInvokerImpl
@Inject
constructor(
    @Named("src") val resourcesDir: String,
    val context: OrchidContext
) : OrchidJavadocInvoker {

    val cacheDir: Path by lazy { OrchidUtils.getCacheDir("javadoc") }
    val outputDir: Path by lazy { OrchidUtils.getTempDir("javadoc", true) }
    val javadocRunner: JavadocdocInvoker by lazy {
        JavadocdocInvokerImpl(cacheDir)
    }

    override fun getRootDoc(
        sourceDirs: List<String>,
        extraArgs: List<String>
    ): JavadocRootdoc? {
        val rootDoc = javadocRunner.loadCachedRootDoc(outputDir)
        if (rootDoc != null) {
            return rootDoc
        } else {
            val sources = sourceDirs.map { File(resourcesDir).toPath().resolve(it) }
            return javadocRunner.getRootDoc(sources, outputDir, extraArgs) { inputStream ->
                InputStreamIgnorer(
                    inputStream
                )
            }
        }
    }

}