package com.eden.orchid.posts.utils

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.OrchidUtils
import java.time.LocalDate

object PostsUtils {
    val latestPostsRegex = "^:latestBlogPosts?(\\((.*)\\))?$".toRegex()

    fun getPostFilename(entry: OrchidResource, baseCategoryPath: String): String {
        val path = OrchidUtils.normalizePath(
            OrchidUtils.normalizePath(entry.reference.originalPath).removePrefix(baseCategoryPath)
        )

        return if (!EdenUtils.isEmpty(path)) {
            val originalFileName = entry.reference.originalFileName
            val eventualName = if (originalFileName.equals("index", true)) "" else "-$originalFileName"
            path.replace("/", "-") + eventualName
        } else {
            entry.reference.originalFileName
        }
    }

    fun parseLatestPostCollectionId(input: String): Pair<String?, Int>? {
        if (input.matches(latestPostsRegex)) {
            val functionContent = latestPostsRegex.matchEntire(input)!!.groupValues[2].trim()

            if (functionContent.isNotBlank()) {
                if (functionContent.contains(",")) {
                    val pieces = functionContent.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    if (pieces.size == 2) {
                        var count = pieces[0].toIntOrNull()
                        var category = pieces[1]
                        if (count == null) {
                            count = pieces[1].toIntOrNull()
                            category = pieces[0]
                        }
                        if (count != null) {
                            return Pair(category, count)
                        }
                    }
                } else {
                    val count = functionContent.toIntOrNull()
                    return if (count != null) Pair(null, count) else Pair(functionContent, 1)
                }
            } else {
                return Pair(null, 1)
            }
        }

        return null
    }
}

fun LocalDate.isToday(): Boolean {
    return this == LocalDate.now()
}
