package com.eden.orchid.bitbucket.publication

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
class BitbucketCloudPublisher
@Inject
constructor(
    context: OrchidContext,
    git: GitFacade,

    @Named("dest")
    destinationDir: String
) : AbstractGitPublisher(context, git, destinationDir, "gitlab-pages", "gitlabPages", 100) {

    override val displayedRemoteUrl: String
        get() {
            throw NotImplementedError()
        }

    override val remoteUrl: String
        get() {
            throw NotImplementedError()
        }

}
