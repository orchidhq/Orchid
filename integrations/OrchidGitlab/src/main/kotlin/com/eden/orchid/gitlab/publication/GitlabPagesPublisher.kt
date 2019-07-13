package com.eden.orchid.gitlab.publication

import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.publication.AbstractGitPublisher
import com.eden.orchid.api.util.GitFacade
import com.eden.orchid.api.util.GitRepoFacade
import com.eden.orchid.api.util.addFile
import javax.inject.Inject
import javax.inject.Named
import javax.validation.constraints.NotBlank

@Description(
    value = "Commit your site directly to Gitlab Pages. It can even keep old versions of your site for versioning documentation.",
    name = "Gitlab Pages"
)
class GitlabPagesPublisher
@Inject
constructor(
    git: GitFacade,

    @Named("dest")
    destinationDir: String,

    @Named("gitlabToken")
    @NotBlank(message = "A Gitlab Personal Access Token is required for deploys, set as \'gitlabToken\' flag.")
    private val gitlabToken: String
) : AbstractGitPublisher(git, destinationDir, "gitlab-pages", "gitlabPages", 100) {

    @Option
    @Description("The user or organization with push access to your repo, used for authenticating with Gitlab.")
    @NotBlank(message = "Must set the Gitlab user or organization.")
    lateinit var username: String

    @Option
    @Description("The repository to push to, as [username/repo], or just [repo] to use the authenticating username.")
    @NotBlank(message = "Must set the Gitlab repository.")
    lateinit var repo: String

    @Option
    @StringDefault("gitlab.com")
    @Description("The URL for a self-hosted Gitlab Enterprise installation.")
    @NotBlank
    lateinit var gitlabUrl: String

    @Option
    @BooleanDefault(true)
    @Description(
        "Whether to have Orchid add the `gitlab-ci.yml` file to deploy the site automatically. For " +
                "repositories that host project sources and the docs website, it is advises to make this false and create" +
                "your own `.gitlab-ci.yml` to suite your needs."
    )
    @NotBlank
    var addPipelineFile: Boolean = true

    private fun getGitlabRepo(): Pair<String, String> {
        val repoParts = repo.split("/".toRegex())
        val repoUsername = if (repoParts.size == 2) repoParts[0] else username
        val repoName = if (repoParts.size == 2) repoParts[1] else repoParts[0]

        return repoUsername to repoName
    }

    override val displayedRemoteUrl: String
        get() {
            val (repoUsername, repoName) = getGitlabRepo()

            return "https://$gitlabUrl/$repoUsername/$repoName.git"
        }

    override val remoteUrl: String
        get() {
            val (repoUsername, repoName) = getGitlabRepo()

            return "https://$username:$gitlabToken@$gitlabUrl/$repoUsername/$repoName.git"
        }

    override fun GitRepoFacade.beforeCommit() {
        if (addPipelineFile) {
            // add the needed .gitlab-ci.yml file so the site gets built and deployed correctly
            addFile(
                ".gitlab-ci.yml", """
                    |# This file is a template, and might need editing before it works on your project.
                    |# Full project: https://gitlab.com/pages/plain-html
                    |pages:
                    |  stage: deploy
                    |  script:
                    |  - mkdir .public
                    |  - cp -r * .public
                    |  - mv .public public
                    |  artifacts:
                    |    paths:
                    |    - public
                    |  only:
                    |  - $branch
                """.trimMargin()
            )
        }
    }
}
