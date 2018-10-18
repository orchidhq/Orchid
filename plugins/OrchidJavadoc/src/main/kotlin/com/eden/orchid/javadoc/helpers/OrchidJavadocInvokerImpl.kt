package com.eden.orchid.javadoc.helpers

import com.copperleaf.dokka.json.Artifact
import com.copperleaf.dokka.json.MavenResolver
import com.copperleaf.dokka.json.MavenResolverImpl
import com.copperleaf.javadoc.json.JavadocdocInvoker
import com.copperleaf.javadoc.json.JavadocdocInvokerImpl
import com.copperleaf.javadoc.json.models.JavadocRootdoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.utilities.InputStreamIgnorer
import com.eden.orchid.utilities.OrchidUtils
import okhttp3.OkHttpClient
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Named

class OrchidJavadocInvokerImpl
@Inject
constructor(
        @Named("src") val resourcesDir: String,
        val client: OkHttpClient,
        val context: OrchidContext
) : OrchidJavadocInvoker {

    val cacheDir: Path by lazy { OrchidUtils.getCacheDir("javadoc") }
    val outputDir: Path by lazy { OrchidUtils.getTempDir("javadoc", true) }
    val resolver: MavenResolver by lazy { MavenResolverImpl(client, cacheDir) }
    val javadocRunner: JavadocdocInvoker by lazy {
        JavadocdocInvokerImpl(resolver, outputDir, listOf(Artifact.from("com.github.copper-leaf.dokka-json:javadoc-json:0.1.23")))
    }

    override fun getRootDoc(sourceDirs: List<String>): JavadocRootdoc? {
        val rootDoc = javadocRunner.loadCachedRootDoc()
        if (rootDoc != null) {
            return rootDoc
        }
        else {
            val sources = sourceDirs.map { File(resourcesDir).toPath().resolve(it) }
            return javadocRunner.getRootDoc(sources, emptyList()) { inputStream -> InputStreamIgnorer(inputStream) }
        }
    }

}