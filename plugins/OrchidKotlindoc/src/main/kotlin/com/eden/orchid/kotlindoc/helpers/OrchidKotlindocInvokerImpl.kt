package com.eden.orchid.kotlindoc.helpers

import com.copperleaf.dokka.json.Artifact
import com.copperleaf.dokka.json.KotlindocInvoker
import com.copperleaf.dokka.json.KotlindocInvokerImpl
import com.copperleaf.dokka.json.MavenResolver
import com.copperleaf.dokka.json.MavenResolverImpl
import com.copperleaf.dokka.json.models.KotlinRootdoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.utilities.InputStreamIgnorer
import com.eden.orchid.utilities.OrchidUtils
import okhttp3.OkHttpClient
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
    val client: OkHttpClient,
    val context: OrchidContext
) : OrchidKotlindocInvoker {

    val cacheDir: Path by lazy { OrchidUtils.getCacheDir("kotlindoc") }
    val outputDir: Path by lazy { OrchidUtils.getTempDir("dokka", true) }
    val resolver: MavenResolver by lazy { MavenResolverImpl(client, cacheDir) }
    val dokkaRunner: KotlindocInvoker by lazy {
        KotlindocInvokerImpl(
            resolver,
            outputDir,
            listOf(Artifact.from("com.github.copper-leaf.dokka-json:dokka-json:0.1.25"))
        )
    }

    override fun getRootDoc(sourceDirs: List<String>, extraArgs: List<String>): KotlinRootdoc? {
        val rootDoc = dokkaRunner.loadCachedRootDoc()
        return if (rootDoc != null) {
            rootDoc
        } else {
            val sources = sourceDirs.map { File(resourcesDir).toPath().resolve(it) }
            dokkaRunner.getRootDoc(sources, args = extraArgs) { inputStream -> InputStreamIgnorer(inputStream) }
        }
    }

}