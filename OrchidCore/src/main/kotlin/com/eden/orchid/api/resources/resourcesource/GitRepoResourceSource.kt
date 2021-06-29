package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.util.GitRepoFacade

/**
 * A ResourceSource that clones a git repo to a temporary directory, then serves file resources out of the root of the
 * cloned repo.
 */
class GitRepoResourceSource(
    val repo: GitRepoFacade,
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

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
