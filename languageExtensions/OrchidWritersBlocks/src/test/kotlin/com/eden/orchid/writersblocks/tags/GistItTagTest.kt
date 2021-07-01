package com.eden.orchid.writersblocks.tags

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.script
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class GistItTagTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    @Test
    @DisplayName("Test GistIt embed")
    fun testDefaultGistIt() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% gistit owner="$owner" repository="$repository" file = "$file" %}
            """.trimMargin()
        )
        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches("body script") {
                    script(src = expectation()) { }
                }
            }
    }

    @Test
    @DisplayName("Test GistIt embed with slice")
    fun testSliceGistIt() {
        val slice = "10:20"
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% gistit owner="$owner" repository="$repository" file="$file" slice="$slice" %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches("body script") {
                    script(src = expectation(slice = slice)) { }
                }
            }
    }

    companion object {
        const val owner = "orchidhq"
        const val repository = "Orchid"
        const val file = "build.gradle.kts"

        fun expectation(
            owner: String = Companion.owner,
            repository: String = Companion.repository,
            file: String = Companion.file,
            branch: String = "master",
            slice: String? = null
        ) = "http://gist-it.appspot.com/https://github.com/" +
            "$owner/" +
            "$repository/raw/" +
            "$branch/" +
            "$file" +
            "?footer=no" +
            if (slice == null) "" else "&slice=$slice"
    }
}
