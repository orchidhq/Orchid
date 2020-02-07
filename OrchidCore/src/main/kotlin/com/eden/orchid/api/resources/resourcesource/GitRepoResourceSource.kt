package com.eden.orchid.api.resources.resourcesource

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.api.util.GitRepoFacade
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.ArrayList

/**
 * A ResourceSource that clones a git repo to a temporary directory, then serves resources out of the
 */
class GitRepoResourceSource(
    val repo: GitRepoFacade,
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
): OrchidResourceSource {

    private val delegate by lazy {
        repo.cloneRepo()
        FileResourceSource(repo.repoDir, priority, scope)
    }

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return delegate.getResourceEntry(context, fileName)
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        return delegate.getResourceEntries(context, dirName, fileExtensions, recursive)
    }

}
