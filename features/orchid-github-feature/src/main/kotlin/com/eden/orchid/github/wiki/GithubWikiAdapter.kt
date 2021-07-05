package com.eden.orchid.github.wiki

import clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.ValidationError
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.validateNotBlank
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.api.util.GitFacade
import com.eden.orchid.api.util.GitRepoFacade
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.wiki.adapter.WikiAdapter
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSummaryPage
import com.eden.orchid.wiki.utils.WikiUtils
import org.apache.commons.io.FilenameUtils
import java.io.File
import javax.inject.Inject

@Suppress(SuppressedWarnings.UNUSED_PARAMETER)
class GithubWikiAdapter
@Inject
constructor(
    val context: OrchidContext,
    val git: GitFacade
) : WikiAdapter {

    @Option
    @Description("The repository to push to, as [username/repo].")
    lateinit var repo: String

    @Option
    @StringDefault("github.com")
    @Description("The URL for a self-hosted Github Enterprise installation.")
    lateinit var githubUrl: String

    @Option
    @StringDefault("master")
    lateinit var branch: String

    override fun validate(context: OrchidContext): List<ValidationError> {
        return super.validate(context) + listOfNotNull(
            validateNotBlank("repo", repo),
            validateNotBlank("githubUrl", githubUrl),
            validateNotBlank("branch", branch),
        )
    }

    override fun getType(): String = "github"

    override fun loadWikiPages(section: WikiSection): Pair<WikiSummaryPage, List<WikiPage>>? {
        val repo = git.clone(remoteUrl, displayedRemoteUrl, branch, false)

        var sidebarFile: File? = null
        var footerFile: File? = null // ignored for now
        val wikiPageFiles = mutableListOf<File>()

        repo.repoDir.toFile()
            .walk()
            .filter { it.isFile }
            .filter { it.exists() }
            .filter { !OrchidUtils.normalizePath(it.absolutePath).contains("/.git/") }
            .forEach { currentFile ->
                if (currentFile.nameWithoutExtension == "_Sidebar") {
                    sidebarFile = currentFile
                } else if (currentFile.nameWithoutExtension == "_Footer") {
                    footerFile = currentFile
                } else {
                    wikiPageFiles.add(currentFile)
                }
            }

        val wikiContent = if (sidebarFile != null) {
            createSummaryFileFromSidebar(repo, section, sidebarFile!!, wikiPageFiles)
        } else {
            createSummaryFileFromPages(repo, section, wikiPageFiles)
        }

        if (footerFile != null) {
            addFooterComponent(wikiContent.second, footerFile!!)
        }

        return wikiContent
    }

// Cloning Wiki
// ---------------------------------------------------------------------------------------------------------------------

    private val displayedRemoteUrl: String
        get() = "https://$githubUrl/$repo.wiki.git"

    private val remoteUrl: String
        get() = "https://$githubUrl/$repo.wiki.git"

// Formatting Wiki files to Orchid's wiki format
// ---------------------------------------------------------------------------------------------------------------------

    private fun createSummaryFileFromSidebar(
        repo: GitRepoFacade,
        section: WikiSection,
        sidebarFile: File,
        wikiPages: MutableList<File>
    ): Pair<WikiSummaryPage, List<WikiPage>> {
        val summary = FileResource(
            OrchidReference(
                context,
                FileResource.pathFromFile(sidebarFile, repo.repoDir.toFile())
            ),
            sidebarFile
        )

        return WikiUtils.createWikiFromSummaryFile(context, section, summary) { linkName, linkTarget, _ ->
            val referencedFile = wikiPages.firstOrNull {
                val filePath = FilenameUtils.removeExtension(it.relativeTo(repo.repoDir.toFile()).path)
                filePath == linkTarget
            }

            if (referencedFile == null) {
                Clog.w("Page referenced in Github Wiki $repo, $linkTarget does not exist")
                StringResource(OrchidReference(context, "wiki/${section.key}/$linkTarget/index.md"), linkName)
            } else {
                FileResource(
                    OrchidReference(
                        context,
                        FileResource.pathFromFile(referencedFile, repo.repoDir.toFile())
                    ),
                    referencedFile
                )
            }
        }
    }

    private fun createSummaryFileFromPages(
        repo: GitRepoFacade,
        section: WikiSection,
        wikiPageFiles: MutableList<File>
    ): Pair<WikiSummaryPage, List<WikiPage>> {
        val wikiPages = wikiPageFiles.map {
            FileResource(
                OrchidReference(
                    context,
                    FileResource.pathFromFile(it, repo.repoDir.toFile())
                ),
                it
            )
        }
        return WikiUtils.createWikiFromResources(context, section, wikiPages)
    }

    private fun addFooterComponent(wikiPages: List<WikiPage>, footerFile: File) {
//        wikiPages.forEach {
//            it.components.add(
//                JSONObject().apply {
//                    put("type", "template")
//
//                }
//            )
//        }
    }
}
