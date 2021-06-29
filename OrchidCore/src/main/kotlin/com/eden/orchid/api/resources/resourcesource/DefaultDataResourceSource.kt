package com.eden.orchid.api.resources.resourcesource

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.OrchidParser
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import java.util.Arrays

@Suppress(SuppressedWarnings.UNCHECKED_KOTLIN)
internal class DefaultDataResourceSource(
    private val delegate: OrchidResourceSource
) : OrchidResourceSource by delegate, DataResourceSource {

    override fun getDatafile(context: OrchidContext, fileName: String): Map<String, Any?>? {
        return delegate
            .flexible()
            .locateResourceEntry(context, fileName, *context.parserExtensions.toTypedArray())
            ?.parseContent(context, null)
    }

    override fun loadData(context: OrchidContext, name: String): Map<String, Any?> {
        var data: Map<String, Any?> = HashMap()

        // load data files from directory. Lowest priority
        val files = getDatafiles(context, name)
        if (files != null) {
            data = EdenUtils.merge(data, files)
        }

        // load data file by name. Medium priority, overrides directory settings
        val file = getDatafile(context, name)
        if (file != null) {
            data = EdenUtils.merge(data, file)
        }

        // load data file for environment. Highest priority, overrides file and directory settings
        val envFile =
            getDatafile(context, name + "-" + context.environment)
        if (envFile != null) {
            data = EdenUtils.merge(data, envFile)
        }
        return data
    }

// Implementation
// ---------------------------------------------------------------------------------------------------------------------

    private fun getDatafiles(context: OrchidContext, directory: String): Map<String, Any?>? {
        val files: List<OrchidResource> = context.getDefaultResourceSource(LocalResourceSource, null)
            .getResourceEntries(context, directory, context.parserExtensions.toTypedArray(), true)
        val allDatafiles: MutableMap<String, Any?> = HashMap()
        for (file in files) {
            file.reference.isUsePrettyUrl = false
            val fileData: Map<String, Any> =
                context.parse(file.reference.extension, file.content)
            val innerPath =
                OrchidUtils.normalizePath(file.reference.path.replace(directory.toRegex(), ""))
            val filePathPieces =
                OrchidUtils.normalizePath(innerPath + "/" + file.reference.fileName).split("/").toTypedArray()
            addNestedDataToMap(allDatafiles, filePathPieces, fileData)
        }
        return allDatafiles
    }

    private fun addNestedDataToMap(
        allDatafiles: MutableMap<String, Any?>,
        pathPieces: Array<String>,
        fileData: Map<String, Any>?
    ) {
        if (fileData != null && pathPieces.size > 0) {
            if (pathPieces.size > 1) {
                if (!allDatafiles.containsKey(pathPieces[0])) {
                    allDatafiles[pathPieces[0]] = HashMap<String, Any>()
                }
                val newArray =
                    Arrays.copyOfRange(pathPieces, 1, pathPieces.size)
                addNestedDataToMap(
                    allDatafiles[pathPieces[0]] as MutableMap<String, Any?>,
                    newArray,
                    fileData
                )
            } else {
                if (fileData.containsKey(OrchidParser.arrayAsObjectKey) && fileData.keys.size == 1) {
                    allDatafiles[pathPieces[0]] = fileData[OrchidParser.arrayAsObjectKey]
                } else {
                    if (allDatafiles.containsKey(pathPieces[0]) && allDatafiles[pathPieces[0]] is Map<*, *>) {
                        for (key in fileData.keys) {
                            (allDatafiles[pathPieces[0]] as MutableMap<String?, Any?>?)!![key] = fileData[key]
                        }
                    } else {
                        allDatafiles[pathPieces[0]] = fileData
                    }
                }
            }
        }
    }
}

fun OrchidResourceSource.useForData(): DataResourceSource {
    return DefaultDataResourceSource(this)
}
