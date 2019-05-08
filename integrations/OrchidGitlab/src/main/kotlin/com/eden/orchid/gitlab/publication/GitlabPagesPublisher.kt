package com.eden.orchid.gitlab.publication

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.publication.AbstractGitPublisher
import com.eden.orchid.api.util.GitFacade
import javax.inject.Inject
import javax.inject.Named

@Description(
    value = "Commit your site directly to Gitlab Pages.",
    name = "gitlab Pages"
)
class GitlabPagesPublisher
@Inject
constructor(
    context: OrchidContext,
    git: GitFacade,

    @Named("dest")
    destinationDir: String
) : AbstractGitPublisher(context, git, destinationDir, "gitlab-pages", "gitlabPages", 100) {

    override val displayedRemoteUrl: String
        get() {
            TODO()
        }

    override val remoteUrl: String
        get() {
            TODO()
        }

}
