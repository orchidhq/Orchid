package com.eden.orchid.bitbucket.publication

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.ValidationError
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.validateNotBlank
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
    git: GitFacade,

    @Named("dest")
    destinationDir: String,

    @Named("bitbucketToken")
    private val bitbucketToken: String
) : AbstractGitPublisher(git, destinationDir, "master", "bitbucketCloud") {

    @Option
    @Description("The user or organization with push access to your repo, used for authenticating with GitHub.")
    lateinit var username: String

    @Option
    @Description("The repository to push to, which looks like [repo.bitbucket.io]")
    lateinit var repo: String

    override val displayedRemoteUrl: String
        get() {
            return "https://$username@bitbucket.org/$username/$repo.git"
        }

    override val remoteUrl: String
        get() {
            return "https://$username:$bitbucketToken@bitbucket.org/$username/$repo.git"
        }

    override fun validate(context: OrchidContext): List<ValidationError> {
        return super.validate(context) + listOfNotNull(
            validateNotBlank("username", username),
            validateNotBlank("repo", repo)
        )
    }
}
