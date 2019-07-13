package com.eden.orchid.github.publication

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.publication.AbstractGitPublisher
import com.eden.orchid.api.util.GitFacade
import javax.inject.Inject
import javax.inject.Named
import javax.validation.constraints.NotBlank

@Description(
    value = "Commit your site directly to Github Pages. It can even keep old versions of your site for versioning documentation.",
    name = "Github Pages"
)
class GithubPagesPublisher
@Inject
constructor(
    git: GitFacade,

    @Named("dest")
    destinationDir: String,

    @Named("githubToken")
    @NotBlank(message = "A GitHub Personal Access Token is required for deploys, set as \'githubToken\' flag.")
    private val githubToken: String
) : AbstractGitPublisher(git, destinationDir, "gh-pages", "githubPages", 100) {

    @Option
    @Description("The user or organization with push access to your repo, used for authenticating with GitHub.")
    @NotBlank(message = "Must set the GitHub user or organization.")
    lateinit var username: String

    @Option
    @Description("The repository to push to, as [username/repo], or just [repo] to use the authenticating username.")
    @NotBlank(message = "Must set the GitHub repository.")
    lateinit var repo: String

    @Option
    @StringDefault("github.com")
    @Description("The URL for a self-hosted Github Enterprise installation.")
    @NotBlank
    lateinit var githubUrl: String

    private fun getGithubRepo() : Pair<String, String> {
        val repoParts = repo.split("/".toRegex())
        val repoUsername = if (repoParts.size == 2) repoParts[0] else username
        val repoName = if (repoParts.size == 2) repoParts[1] else repoParts[0]

        return repoUsername to repoName
    }

    override val displayedRemoteUrl: String
        get() {
            val (repoUsername, repoName) = getGithubRepo()

            return "https://$githubUrl/$repoUsername/$repoName.git"
        }

    override val remoteUrl: String
        get() {
            val (repoUsername, repoName) = getGithubRepo()

            return "https://$username:$githubToken@$githubUrl/$repoUsername/$repoName.git"
        }

}
