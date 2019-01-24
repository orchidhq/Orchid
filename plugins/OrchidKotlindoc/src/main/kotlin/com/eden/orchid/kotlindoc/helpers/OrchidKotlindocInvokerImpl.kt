package com.eden.orchid.kotlindoc.helpers

import com.copperleaf.dokka.json.KotlindocInvoker
import com.copperleaf.dokka.json.KotlindocInvokerImpl
import com.copperleaf.dokka.json.models.KotlinRootdoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.utilities.InputStreamIgnorer
import com.eden.orchid.utilities.OrchidUtils
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class OrchidKotlindocInvokerImpl
@Inject
constructor(
    @Named("src") val resourcesDir: String,
    val context: OrchidContext
) : OrchidKotlindocInvoker {

    val cacheDir: Path by lazy { OrchidUtils.getCacheDir("kotlindoc") }
    val outputDir: Path by lazy { OrchidUtils.getTempDir("kotlindoc", true) }
    val dokkaRunner: KotlindocInvoker by lazy { KotlindocInvokerImpl(cacheDir) }

    override fun getRootDoc(
        sourceDirs: List<String>,
        extraArgs: List<String>
    ): KotlinRootdoc? {
        val rootDoc = dokkaRunner.loadCachedRootDoc(outputDir)
        return if (rootDoc != null) {
            rootDoc
        } else {
            val sources = sourceDirs.map { File(resourcesDir).toPath().resolve(it) }
            dokkaRunner.getRootDoc(
                sources,
                outputDir,
                extraArgs
            ) { inputStream -> InputStreamIgnorer(inputStream) }
        }
    }

}