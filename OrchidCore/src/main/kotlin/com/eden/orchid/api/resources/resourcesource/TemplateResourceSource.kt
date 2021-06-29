package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource

interface TemplateResourceSource : OrchidResourceSource {

    /**
     * Splits [fileName] by comma (`,`) and passes the resulting template list through to
     * [getResourceEntry(context, templateSubDir, possibleFileNames)] with no templateSubDir
     */
    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource?

    /**
     * Not available for TemplateResourceSource, only single templates can be located.
     */
    @Deprecated("Not available for TemplateResourceSource, only single templates can be located.")
    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource>

    /**
     * Locates a single matching template given a list of possible filenames. Templates can optionally be scoped so a
     * template subdirectory.
     *
     * Template are located in the following manner:
     * - For each possible filename in [possibleFileNames]:
     *   1. An exact match on the filename
     *   2. A match on the filename using the file extension preferred by the theme
     *   3. A match on the filename using the default template file extension
     *   4. If a [templateSubDir] is provided, repeat steps (1), (2), and (3) but in that subdirectory
     */
    fun getResourceEntry(
        context: OrchidContext,
        templateSubDir: String?,
        possibleFileNames: List<String>
    ): OrchidResource?
}
