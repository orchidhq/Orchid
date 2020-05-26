package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext

interface DataResourceSource: OrchidResourceSource {

    /**
     * Loads a single resource by name and parses its markup into a map of data according to the markup type described
     * by its own file extension.
     */
    fun getDatafile(context: OrchidContext, fileName: String): Map<String, Any?>?

    /**
     * Loads multiple markup files and parses them into data according to their own file extension, and then merges
     * these individual maps into a single combined map.
     *
     * Files are located in the following manner:
     *   1. Folder at `[name]/`
     *   2. Single file at `[name]` overrides values from (1)
     *   3. Single file at `[name]-{context.environment}` overrides values from (1) and (2)
     * * Single resources are found using a flexible lookup. See [FlexibleResourceSource].
     *
     * Files are merged in the following manner:
     *   - The root files for (2) and (3) are merged normally with a deep-merge. Map values are merged, with keys from
     *      the higher-priority resource taking precedence when there is a conflict. Arrays are combined.
     *   - With similar logic for merging two maps, the folder resources are combined into a single map, where the
     *      resource path defines its location in the map of data. For example. A file at `config/site.yml` is loaded as
     *      if it were a map under the `site` key in the root `config.yml` file. Its entire contents are nested under
     *      the `site` property when merged.
     */
    fun loadData(context: OrchidContext, name: String): Map<String, Any?>
}
