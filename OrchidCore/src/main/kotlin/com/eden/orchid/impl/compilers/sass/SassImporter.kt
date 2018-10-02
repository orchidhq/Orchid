package com.eden.orchid.impl.compilers.sass

import com.eden.common.util.EdenPair
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.OrchidUtils
import io.bit3.jsass.importer.Import
import io.bit3.jsass.importer.Importer

import javax.inject.Inject
import java.util.Collections

class SassImporter @Inject
constructor(private val context: OrchidContext) : Importer {

    override fun apply(url: String, previous: Import): Collection<Import>? {
        val thisItem = splitPath(url)

        val availableFiles = arrayOf(
            thisItem.first + "/" + thisItem.second + ".scss",
            thisItem.first + "/" + thisItem.second + ".sass",
            thisItem.first + "/" + "_" + thisItem.second + ".scss",
            thisItem.first + "/" + "_" + thisItem.second + ".sass"
        )

        for (availableFile in availableFiles) {
            val pathStart = OrchidUtils.normalizePath(splitPath(previous.absoluteUri.path).first)
            val pathEnd = OrchidUtils.normalizePath(availableFile)
            var absoluteUri = OrchidUtils.normalizePath("$pathStart/$pathEnd")

            if (absoluteUri!!.contains("//")) {
                absoluteUri = absoluteUri.replace("//".toRegex(), "/")
            }
            if (absoluteUri.startsWith("/")) {
                absoluteUri = absoluteUri.substring(1)
            }

            val importedResource = context.getResourceEntry("assets/css/$absoluteUri")

            if (importedResource != null) {
                var content = importedResource.content

                if (importedResource.shouldPrecompile()) {
                    content = context.compile(
                        importedResource.precompilerExtension,
                        content,
                        importedResource.embeddedData
                    )
                }

                try {
                    val newURI = "" + OrchidUtils.normalizePath(absoluteUri)!!
                    val newImport = Import(newURI, newURI, content)
                    return listOf(newImport)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        return null
    }

    private fun splitPath(name: String): EdenPair<String, String> {
        var name = name
        name = name.replace("\\\\\\\\".toRegex(), "/")
        name = name.replace("\\\\".toRegex(), "/")

        if (name.contains("/")) {
            val pieces = name.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var path = ""

            for (i in 0 until pieces.size - 1) {
                path += pieces[i].replace("_".toRegex(), "") + "/"
            }
            val fileName = pieces[pieces.size - 1].replace("_".toRegex(), "")

            return EdenPair(OrchidUtils.normalizePath(path), OrchidUtils.normalizePath(fileName))
        } else {
            return EdenPair("", name)
        }
    }
}
