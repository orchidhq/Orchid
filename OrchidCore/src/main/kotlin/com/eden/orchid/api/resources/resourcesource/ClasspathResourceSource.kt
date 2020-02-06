package com.eden.orchid.api.resources.resourcesource

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.ClasspathResource
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.OrchidResource
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.ArrayList

open class ClasspathResourceSource(
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return javaClass.classLoader.getResource(fileName)?.let { ClasspathResource(context, it) }
    }

    override fun getResourceEntries(context: OrchidContext, dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        val entries = ArrayList<OrchidResource>()

//        javaClass
//            .classLoader
//            .getResources(dirName)
//            .toList()
//            .map { ClasspathResource(context, it) }

        return entries
    }
}
