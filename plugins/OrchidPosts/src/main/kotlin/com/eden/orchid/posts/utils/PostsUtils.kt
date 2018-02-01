package com.eden.orchid.posts.utils

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.OrchidUtils
import java.time.LocalDate

object PostsUtils {

    fun getPostFilename(entry: OrchidResource, baseCategoryPath: String): String {
        val path = OrchidUtils.normalizePath(OrchidUtils.normalizePath(entry.reference.originalPath).removePrefix(baseCategoryPath))

        return if (!EdenUtils.isEmpty(path))
            path.replace("/", "-") + "-" + entry.reference.originalFileName
        else
            entry.reference.originalFileName
    }
}

fun LocalDate.isToday(): Boolean {
    return this == LocalDate.now()
}
