package com.eden.orchid.netlifycms.api

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.annotations.Delete
import com.eden.orchid.api.server.annotations.Get
import com.eden.orchid.api.server.annotations.Post
import com.eden.orchid.api.server.annotations.Put
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.readToString
import com.google.inject.name.Named
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.file.Files
import java.util.Base64
import javax.inject.Inject

class NetlifyCmsApiController
@Inject
constructor(
        val context: OrchidContext,
        @Named("src") val resourcesDir: String
) : OrchidController(1000) {

    override fun getPathNamespace(): String {
        return "api"
    }

// Get file lists
//----------------------------------------------------------------------------------------------------------------------

    @Get(path = "/files/**localFilename")
    fun getFiles(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest, localFilename: String): OrchidResponse {
        val locatedResources = JSONArray()

        val resources = context.getResourceEntries(localFilename, null, false, LocalResourceSource)
        if (!EdenUtils.isEmpty(resources)) {
            resources.forEach {
                val localResource = JSONObject()
                localResource.put("name", "${it.reference.originalFileName}.${it.reference.extension}")
                localResource.put("path", it.reference.originalFullFileName)
                localResource.put("label", it.reference.title)
                localResource.put("title", it.reference.title)
                localResource.put("sha", "")
                localResource.put("size", 0)
                localResource.put("stats", JSONObject())
                localResource.put("type", "file")

                locatedResources.put(localResource)
            }
        }

        return OrchidResponse(context).json(locatedResources)
    }

// CRUD with single files
//----------------------------------------------------------------------------------------------------------------------

    @Get(path = "/file/**localFilename")
    fun getFile(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest, localFilename: String): OrchidResponse {
        val resource = context.getResourceEntry(localFilename, LocalResourceSource)
        if (resource != null) {
            return OrchidResponse(context).content(resource.getContentStream().readToString())
        }
        else {
            return OrchidResponse(context).content("Not found").status(404)
        }
    }

    @Delete(path = "/file/**localFilename")
    fun deleteFile(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest, localFilename: String): OrchidResponse {
        val resource = context.getResourceEntry(localFilename, LocalResourceSource)
        if (resource != null) {
            if (resource.canDelete()) {
                resource.delete()
                return OrchidResponse(context).content("Successfully deleted")
            }
            else {
                return OrchidResponse(context).content("Cannot delete resource").status(500)
            }
        }
        else {
            return OrchidResponse(context).content("Not found").status(404)
        }
    }

    @Put(path = "/file/**localFilename")
    fun updateFile(request: OrchidRequest, localFilename: String): OrchidResponse {
        val resource = context.getResourceEntry(localFilename, LocalResourceSource)
        if (resource != null) {
            if (resource.canUpdate()) {
                val contentBase64 = request.input("content")
                val contentBytes = Base64.getDecoder().decode(contentBase64)
                val contentStream = contentBytes.inputStream()

                resource.update(contentStream)
                return OrchidResponse(context).content("Successfully updated")
            }
            else {
                return OrchidResponse(context).content("Cannot update resource").status(500)
            }
        }
        else {
            return createFile(request, localFilename)
        }
    }

    @Post(path = "/file/**localFilename")
    fun createFile(request: OrchidRequest, localFilename: String): OrchidResponse {
        val newFile = File(resourcesDir + "/" + OrchidUtils.normalizePath(localFilename))
        if (!newFile.exists()) {
            newFile.absoluteFile.parentFile.mkdirs()
        }

        try {
            Files.write(newFile.toPath(), Base64.getDecoder().decode(request.input("content")))
            return OrchidResponse(context).content("Successfully created")
        }
        catch (e: Exception) {
            e.printStackTrace()
            return OrchidResponse(context).content("Could not create file").status(500)
        }
    }

}
