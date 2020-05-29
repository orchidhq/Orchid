package com.eden.orchid.gitlab.wiki

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.ResourceTransformation
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.api.util.GitFacade
import com.eden.orchid.api.util.GitRepoFacade
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.wiki.adapter.WikiAdapter
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSummaryPage
import com.eden.orchid.wiki.utils.WikiUtils
import org.apache.commons.io.FilenameUtils
import java.io.File
import javax.inject.Inject
import javax.validation.constraints.NotBlank

class GitlabWikiAdapter
@Inject
constructor(
    val context: OrchidContext,
    val git: GitFacade
) : WikiAdapter {

    @Option
    @Description("The repository to push to, as [username/repo].")
    @NotBlank(message = "Must set the Gitlab repository.")
    lateinit var repo: String

    @Option
    @StringDefault("gitlab.com")
    @Description("The URL for a self-hosted Gitlab Enterprise installation.")
    @NotBlank
    lateinit var gitlabUrl: String

    @Option
    @StringDefault("master")
    lateinit var branch: String

    override fun getType(): String = "gitlab"

    private fun createAssetManagerDelegate(context: OrchidContext): AssetManagerDelegate {
        return AssetManagerDelegate(context, this, "wikiAdapter", null)
    }

    override fun loadWikiPages(section: WikiSection): Pair<WikiSummaryPage, List<WikiPage>>? {
        val repo = git.clone(remoteUrl, displayedRemoteUrl, branch, false)

        var sidebarFile: File? = null
        val wikiPageFiles = mutableListOf<File>()
        val wikiUploads = mutableListOf<File>()

        repo.repoDir.toFile()
            .walk()
            .filter { it.isFile }
            .filter { it.exists() }
            .filter { !OrchidUtils.normalizePath(it.absolutePath).contains("/.git/") }
            .forEach { currentFile ->
                if (currentFile.nameWithoutExtension == "_Sidebar") {
                    sidebarFile = currentFile
                } else if (currentFile.pathInRepo(repo).startsWith("uploads/")) {
                    wikiUploads.add(currentFile)
                } else {
                    wikiPageFiles.add(currentFile)
                }
            }

        val wikiContent = if (sidebarFile != null) {
            createSummaryFileFromSidebar(repo, section, sidebarFile!!, wikiPageFiles, wikiUploads)
        } else {
            createSummaryFileFromPages(repo, section, wikiPageFiles, wikiUploads)
        }

        return wikiContent
    }

// Cloning Wiki
//----------------------------------------------------------------------------------------------------------------------

    private val displayedRemoteUrl: String
        get() = "https://$gitlabUrl/$repo.wiki.git"

    private val remoteUrl: String
        get() = "https://$gitlabUrl/$repo.wiki.git"

// Formatting Wiki files to Orchid's wiki format
//----------------------------------------------------------------------------------------------------------------------

    private fun createSummaryFileFromSidebar(
        repo: GitRepoFacade,
        section: WikiSection,
        sidebarFile: File,
        wikiPages: MutableList<File>,
        wikiUploads: List<File>
    ): Pair<WikiSummaryPage, List<WikiPage>> {
        val summary = FileResource(
            OrchidReference(
                context,
                FileResource.pathFromFile(sidebarFile, repo.repoDir.toFile())
            ),
            sidebarFile
        )

        return WikiUtils
            .createWikiFromSummaryFile(context, section, summary) { linkName, linkTarget, _ ->
                val referencedFile = wikiPages.firstOrNull {
                    val filePath = FilenameUtils.removeExtension(it.relativeTo(repo.repoDir.toFile()).path)
                    filePath == linkTarget
                }

                if (referencedFile == null) {
                    Clog.w("Page referenced in Gitlab Wiki $repo, $linkTarget does not exist")
                    StringResource(OrchidReference(context, "wiki/${section.key}/$linkTarget/index.md"), linkName)
                } else {
                    FileResource(
                        OrchidReference(
                            context,
                            FileResource.pathFromFile(referencedFile, repo.repoDir.toFile())
                        ),
                        referencedFile
                    )
                }.wrapResourceToCopyImagesOnRender(repo, wikiUploads)
            }
    }

    private fun createSummaryFileFromPages(
        repo: GitRepoFacade,
        section: WikiSection,
        wikiPageFiles: List<File>,
        wikiUploads: List<File>
    ): Pair<WikiSummaryPage, List<WikiPage>> {
        val wikiPages = wikiPageFiles
            .map {
                FileResource(
                    OrchidReference(
                        context,
                        FileResource.pathFromFile(it, repo.repoDir.toFile())
                    ),
                    it
                ).wrapResourceToCopyImagesOnRender(repo, wikiUploads)
            }

        return WikiUtils.createWikiFromResources(context, section, wikiPages)
    }

    private fun File.pathInRepo(repo: GitRepoFacade): String {
        return OrchidUtils.normalizePath(absoluteFile.absolutePath.removePrefix(repo.repoDir.toFile().absolutePath))
    }

    private fun OrchidResource.wrapResourceToCopyImagesOnRender(
        repo: GitRepoFacade,
        wikiUploads: List<File>
    ): OrchidResource {
        return ResourceTransformation(
            this,
            contentPostTransformations = mutableListOf(
                WikiUtils.handleImages { imageTarget, _ ->
                    wikiUploads
                        .find { it.pathInRepo(repo) == OrchidUtils.normalizePath(imageTarget) }
                        ?.let {
                            FileResource(
                                OrchidReference(
                                    context,
                                    FileResource.pathFromFile(it, repo.repoDir.toFile())
                                ),
                                it
                            )
                        }
                        ?.let { AssetPage(createAssetManagerDelegate(context), it, "image", null).also { it.configureReferences() } }
                }
            )
        )
    }
}
