package com.eden.orchid.github.wiki

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.annotations.Validate
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.api.util.GitFacade
import com.eden.orchid.api.util.GitRepoFacade
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.wiki.adapter.WikiAdapter
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSummaryPage
import org.apache.commons.io.FilenameUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import java.io.File
import javax.inject.Inject
import javax.validation.constraints.NotBlank

@Validate
class GithubWikiAdapter
@Inject
constructor(
    val context: OrchidContext,
    val git: GitFacade
) : WikiAdapter {

    @Option
    @Description("The repository to push to, as [username/repo].")
    @NotBlank(message = "Must set the GitHub repository.")
    lateinit var repo: String

    @Option
    @StringDefault("github.com")
    @Description("The URL for a self-hosted Github Enterprise installation.")
    @NotBlank
    lateinit var githubUrl: String

    @Option
    @StringDefault("master")
    lateinit var branch: String

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

        return if (sidebarFile != null) {
            createSummaryFileFromSidebar(repo, section, sidebarFile!!, wikiPageFiles, footerFile)
        } else {
            createSummaryFileFromPages(repo, section, wikiPageFiles, footerFile)
        }
    }

// Cloning Wiki
//----------------------------------------------------------------------------------------------------------------------

    private val displayedRemoteUrl: String
        get() = "https://$githubUrl/$repo.wiki.git"

    private val remoteUrl: String
        get() = "https://$githubUrl/$repo.wiki.git"

// Formatting Wiki files to Orchid's wiki format
//----------------------------------------------------------------------------------------------------------------------

    private fun createSummaryFileFromSidebar(
        repo: GitRepoFacade,
        section: WikiSection,
        sidebarFile: File,
        wikiPageFiles: MutableList<File>,
        footerFile: File?
    ): Pair<WikiSummaryPage, List<WikiPage>> {
        val summaryPageReference = OrchidReference(context, "wiki/${section.key}.html")
        val originalSummaryResource = FileResource(context, sidebarFile, repo.repoDir.toFile())

        val compiledContent = originalSummaryResource.compileContent(null)

        val (reformattedContent, wiki) = getPagesReferencedInSidebar(repo, section, compiledContent, wikiPageFiles)

        val formattedSummaryResource = StringResource(reformattedContent, summaryPageReference)

        val summaryPage = WikiSummaryPage(
            section.key,
            formattedSummaryResource,
            section.title
        )
        summaryPage.reference = OrchidReference(context, "wiki/${section.key}.html")

        return summaryPage to wiki
    }

    private fun getPagesReferencedInSidebar(repo: GitRepoFacade, section: WikiSection, sourceContent: String, wikiPages: List<File>) : Pair<String, List<WikiPage>> {
        val doc = Jsoup.parse(sourceContent)

        val links = doc.select("a[href]")
        val referencedPages = mutableListOf<WikiPage>()

        var previous: WikiPage? = null

        var i = 0

        for (a in links) {
            if (OrchidUtils.isExternal(a.attr("href"))) continue

            val pageTitle = a.text()
            val pagePath = a.attr("href")

            val referencedFile = wikiPages.firstOrNull {
                val requestedPath = pagePath
                val filePath = FilenameUtils.removeExtension(it.relativeTo(repo.repoDir.toFile()).path)
                requestedPath == filePath
            }

            if(referencedFile == null) {
                Clog.w("Page referenced in Github Wiki $repo, $pagePath does not exist")
                a.replaceWith(TextNode(pageTitle))
            }
            else {
                val referencedPageResource = FileResource(context, referencedFile, repo.repoDir.toFile())
                val referencedPage = WikiPage(referencedPageResource, "", section.key, i+1).also {
                    it.reference.path = "wiki/${section.key}"
                }
                referencedPages.add(referencedPage)

                a.attr("href", referencedPage.link)
                i++

                if (previous != null) {
                    previous.next = referencedPage
                    referencedPage.previous = previous

                    previous = referencedPage
                } else {
                    previous = referencedPage
                }
            }
        }

        return doc.toString() to referencedPages
    }

    private fun createSummaryFileFromPages(
        repo: GitRepoFacade,
        section: WikiSection,
        wikiPageFiles: MutableList<File>,
        footerFile: File?
    ): Pair<WikiSummaryPage, List<WikiPage>> {
        wikiPageFiles.sortBy { it.nameWithoutExtension }
        val wikiPages = createWikiPages(repo, section, wikiPageFiles, footerFile)

        var summaryPageContent = "<ul>"
        for(page in wikiPages) {
            summaryPageContent += """<li><a href="${page.link}">${page.title}</a></li>"""
        }
        summaryPageContent += "</ul>"

        val summaryPage = WikiSummaryPage(
            section.key,
            StringResource(context, "wiki/${section.key}.html", summaryPageContent),
            "Summary"
        )

        return summaryPage to emptyList()
    }

    private fun createWikiPages(
        repo: GitRepoFacade,
        section: WikiSection,
        wikiPageFiles: List<File>,
        footerFile: File?
    ): List<WikiPage> {
        return wikiPageFiles.map {
            val sourceResource = FileResource(context, it, repo.repoDir.toFile())
            WikiPage(sourceResource, "", section.key, 1).also {
                it.reference = OrchidReference(context, "wiki/${section.key}/${sourceResource.reference.path}")
            }
        }
    }

}
