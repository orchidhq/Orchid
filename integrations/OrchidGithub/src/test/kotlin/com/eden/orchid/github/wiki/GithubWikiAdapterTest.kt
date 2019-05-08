package com.eden.orchid.github.wiki

import com.eden.common.util.EdenPair
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.util.CliGitFacade
import com.eden.orchid.api.util.GitFacade
import com.eden.orchid.wiki.model.WikiSection
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isNotNull

class GithubWikiAdapterTest {

    private lateinit var context: OrchidContext
    private lateinit var extractor: OptionsExtractor
    private lateinit var tempDir: String
    private lateinit var git: GitFacade
    private lateinit var underTest: GithubWikiAdapter
    private lateinit var originalSection: WikiSection

    @BeforeEach
    fun setUp() {
        extractor = mock(OptionsExtractor::class.java)
        context = mock(OrchidContext::class.java).apply {
            `when`(resolve(OptionsExtractor::class.java)).thenReturn(extractor)
            `when`(getEmbeddedData(anyString(), anyString())).thenReturn(EdenPair("", emptyMap()))
        }
        tempDir = "build/GithubWikiAdapterTest"
        git = CliGitFacade(tempDir)
        underTest = GithubWikiAdapter(context, git)
        originalSection = WikiSection().apply {
            key = ""
            title = ""
        }
    }

    @AfterEach
    fun tearDown() {

    }

    @Test
    fun testWikiWithSidebar() {
        underTest.githubUrl = "github.com"
        underTest.repo = "google/guice"
        underTest.branch = "master"

        val newSection = underTest.loadWikiPages(originalSection)

        expectThat(newSection)
            .isNotNull()
            .get { first }
        expectThat(newSection)
            .isNotNull()
            .get { second }
            .hasSize(68)

//        Thread.sleep(60000)
    }



}