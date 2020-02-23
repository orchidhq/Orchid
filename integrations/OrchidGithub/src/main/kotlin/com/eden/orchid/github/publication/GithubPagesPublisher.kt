package com.eden.orchid.github.publication

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.publication.AbstractGitPublisher
import com.eden.orchid.api.util.GitFacade
import com.eden.orchid.api.util.GitRepoFacade
import com.eden.orchid.api.util.addFile
import java.net.URI
import java.net.URL
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
) : AbstractGitPublisher(git, destinationDir, "gh-pages", "githubPages") {

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

    @Option
    @BooleanDefault(true)
    @Description(
        "Whether to have Orchid add the `CNAME` file automatically. A CNAME will be added to the deployment when " +
                "all of the following criteria are met:\n" +
                "  1) The `addCnameFile` flag is set to true\n" +
                "  2) A CNAME file does not already exist in the generated site root\n" +
                "  3) The site's base URL is not a `.github.io` URL\n"
    )
    var addCnameFile: Boolean = true

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

    override fun GitRepoFacade.beforeCommit(context: OrchidContext) {
        val hasCnameFile = repoDir.resolve("CNAME").toFile().exists()
        val domain = URL(context.baseUrl).host ?: ""
        val hasDefaultDomain = domain.endsWith(".github.io", ignoreCase = true)

        if (addCnameFile && !hasCnameFile && !hasDefaultDomain) {
            // add the needed CNAME file so custom domains are routed correctly
            addFile(
                "CNAME",
                domain
            )
        }
    }

}
