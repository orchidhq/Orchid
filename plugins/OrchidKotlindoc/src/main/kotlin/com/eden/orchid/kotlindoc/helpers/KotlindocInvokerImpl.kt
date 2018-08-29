package com.eden.orchid.kotlindoc.helpers

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.kotlindoc.model.KotlinClassdoc
import com.eden.orchid.kotlindoc.model.KotlinPackagedoc
import com.eden.orchid.kotlindoc.model.KotlinRootdoc
import com.eden.orchid.utilities.InputStreamPrinter
import com.eden.orchid.utilities.OrchidUtils
import com.google.inject.name.Named
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import org.json.JSONObject
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import java.nio.file.Path
import java.util.ArrayDeque
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class KotlindocInvokerImpl
@Inject
constructor(
        @Named("src") val resourcesDir: String,
        val client: OkHttpClient,
        val context: OrchidContext) : KotlindocInvoker {

    val repos = listOf(
            "https://jcenter.bintray.com/",
            "https://jitpack.io/"
    )

    override fun getRootDoc(sourceDirs: List<String>): KotlinRootdoc? {
        val dokkaOutputPath = OrchidUtils.getTempDir("dokka", true)
        val dokkaJarPaths = getMavenJars("com.github.copper-leaf:dokka-json:0.1.0")

        executeDokka(dokkaJarPaths, dokkaOutputPath, sourceDirs)

        return getKotlinRootdoc(dokkaOutputPath)
    }


// Download jars from Maven
//----------------------------------------------------------------------------------------------------------------------

    private fun getMavenJars(target: String): List<String> {
        val processedTargets = HashSet<String>()
        val targetsToProcess = ArrayDeque<String>()
        targetsToProcess.add(target)

        val resolvedJars = ArrayList<String>()

        while (targetsToProcess.peek() != null) {
            val currentTarget = targetsToProcess.pop()

            // we've already processed this artifact, skip this iteration
            if (processedTargets.contains(currentTarget)) continue
            processedTargets.add(currentTarget)

            // otherwise resolve its dependencies and download this jar
            val groupId = currentTarget.split(":").getOrElse(0) { "" }
            val artifactId = currentTarget.split(":").getOrElse(1) { "" }
            val version = currentTarget.split(":").getOrElse(2) { "" }

            targetsToProcess.addAll(getTransitiveDependencies(groupId, artifactId, version))
            val downloadedJarPath = downloadJar(groupId, artifactId, version)
            if (!EdenUtils.isEmpty(downloadedJarPath)) {
                resolvedJars.add(downloadedJarPath)
            }
        }

        return resolvedJars
    }

    private fun getTransitiveDependencies(groupId: String, artifactId: String, version: String): List<String> {
        for (repo in repos) {
            val pomUrl = "$repo/${groupId.replace(".", "/")}/$artifactId/$version/$artifactId-$version.pom"
            client.newCall(Request.Builder().url(pomUrl).build()).execute().use {
                if (it.isSuccessful) {
                    val mavenMetadataXml = it.body()?.string() ?: ""

                    val doc = DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .parse(InputSource(StringReader(mavenMetadataXml)))

                    val itemsTypeT1 = XPathFactory
                            .newInstance()
                            .newXPath()
                            .evaluate("/project/dependencies/dependency", doc, XPathConstants.NODESET) as NodeList

                    val transitiveDependencies = ArrayList<String>()

                    for (i in 0 until itemsTypeT1.length) {
                        var childGroupId = ""
                        var childArtifactId = ""
                        var childVersion = ""
                        var scope = ""
                        for (j in 0 until itemsTypeT1.item(i).childNodes.length) {
                            val name = itemsTypeT1.item(i).childNodes.item(j).nodeName
                            val value = itemsTypeT1.item(i).childNodes.item(j).textContent
                            when (name) {
                                "groupId"    -> childGroupId = value
                                "artifactId" -> childArtifactId = value
                                "version"    -> childVersion = value
                                "scope"      -> scope = value
                            }
                        }

                        if (scope == "compile") {
                            transitiveDependencies.add("$childGroupId:$childArtifactId:$childVersion")
                        }
                    }

                    return transitiveDependencies
                }
            }
        }

        return emptyList()
    }

    private fun downloadJar(groupId: String, artifactId: String, version: String): String {
        for (repo in repos) {
            val downloadedFile = File(OrchidUtils.getCacheDir("kotlindoc").toFile(), "$artifactId-$version.jar")

            if (downloadedFile.exists()) {
                return downloadedFile.absolutePath
            }
            else {
                val jarUrl = "$repo/${groupId.replace(".", "/")}/$artifactId/$version/$artifactId-$version.jar"
                client.newCall(Request.Builder().url(jarUrl).build()).execute().use {
                    if (it.isSuccessful) {
                        val sink = Okio.buffer(Okio.sink(downloadedFile))
                        sink.writeAll(it.body()!!.source())
                        sink.close()

                        return downloadedFile.absolutePath
                    }
                }
            }
        }

        return ""
    }

// Run Dokka
//----------------------------------------------------------------------------------------------------------------------

    private fun executeDokka(dokkaJarPath: List<String>, dokkaOutputPath: Path, sourceDirs: List<String>) {
        val process = ProcessBuilder()
                .command(
                        "java",
                        "-classpath", dokkaJarPath.joinToString(File.pathSeparator), // classpath of downloaded jars
                        "org.jetbrains.dokka.MainKt", // Dokka main class
                        "-format", "json", // JSON format (so Orchid can pick it up afterwards)
                        "-src", sourceDirs.joinToString(separator = File.pathSeparator), // the sources to process
                        "-output", dokkaOutputPath.toFile().absolutePath // where Orchid will find them later
                )
                .directory(File(resourcesDir))
                .start()

        Executors.newSingleThreadExecutor().submit(InputStreamPrinter(process.inputStream))
        process.waitFor()
    }

// Process Dokka output to a model Orchid can use
//----------------------------------------------------------------------------------------------------------------------

    private fun getKotlinRootdoc(dokkaOutputPath: Path): KotlinRootdoc {
        return KotlinRootdoc(
                getDokkaPackagePages(dokkaOutputPath),
                getDokkaClassPages(dokkaOutputPath)
        )
    }

    private fun getDokkaPackagePages(dokkaOutputPath: Path): List<KotlinPackagedoc> {
        val packagePagesList = ArrayList<KotlinPackagedoc>()
        dokkaOutputPath
                .toFile()
                .walkTopDown()
                .filter { it.isFile && it.name == "index.json" }
                .map { JSONObject(it.readText()) }
                .filter { it["kind"] == "Package" }
                .map { KotlinPackagedoc(it["qualifiedName"].toString()) }
                .toCollection(packagePagesList)

        packagePagesList.forEach {
            Clog.v("package: {}", it.name)
        }

        return packagePagesList
    }

    private fun getDokkaClassPages(dokkaOutputPath: Path): List<KotlinClassdoc> {
        val classPagesList = ArrayList<KotlinClassdoc>()
        dokkaOutputPath
                .toFile()
                .walkTopDown()
                .filter { it.isFile && it.name != "index.json" }
                .map { JSONObject(it.readText()) }
                .filter { it["kind"] == "Class" }
                .map { KotlinClassdoc(it["qualifiedName"].toString()) }
                .toCollection(classPagesList)

        classPagesList.forEach {
            Clog.v("class: {}", it.name)
        }

        return classPagesList
    }

}