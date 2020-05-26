package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource

interface FlexibleResourceSource : OrchidResourceSource{

    /**
     * Locates a single matching template given a list of possible file extensions. The fileName passed here should not
     * have any file extension on it, as the possible extensions will be appended to it to check for existence. The
     * first match will be returned.
     */
    fun locateResourceEntry(
        context: OrchidContext,
        fileName: String,
        vararg fileExtensions: String = context.compilerExtensions.toTypedArray()
    ): OrchidResource?

    /**
     * This method completely bypasses the normal resource lookup system, and instead directly uses file APIs. This
     * should be avoided, as it cannot be cached and isn't known by the rest of the resource framework.
     * locateResourceEntry() might be a better alternative, but there isn't a direct replacement now. Just try to figure
     * out how to build the plugin without needing this kind of functionality.
     */
    @Deprecated("Avoid looking up files outside of the resource root. Try to make locateResourceEntry() work instead.")
    fun findClosestFile(
        context: OrchidContext,
        filename: String,
        baseDir: String = context.sourceDir,
        _maxIterations: Int = 10
    ): OrchidResource?
}
