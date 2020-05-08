package com.eden.orchid.posts

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlHeadMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.link
import kotlinx.html.title
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Posts generator")
class FeedsGeneratorTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(),  PostsModule()) {

    @Test
    @DisplayName("Files, formatted correctly in the `posts` directory, gets rendered correctly without any configuration.")
    fun test01() {
        resource("posts/2018-01-01-post-one.md")

        resource(
            "homepage.md",
            """
            |# Check the `<head>` for links to post feeds!
            """.trimMargin(),
            """
            |{
            |   "metaComponents": [
            |       { "type": "feedLinks" }
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/2018/1/1/post-one/index.html")
            .pageWasRendered("/index.html") {
                htmlHeadMatches("head link[rel=alternate]") {
                    link(rel="alternate", type="application/rss+xml",  href="http://orchid.test/rss.xml") { title = " (RSS)" }
                    link(rel="alternate", type="application/atom+xml", href="http://orchid.test/atom.xml") { title = " (Atom)" }
                }
            }
    }
}
