package com.eden.orchid.impl.generators

import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class SitemapGeneratorTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    withGenerator<SitemapGenerator>()
) {

    @Test
    @DisplayName("Homepage generator creates a homepage and 404 pages by default")
    fun test01() {
        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"" }
                    }
            }
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/404.html")

            .pageWasRendered("/robots.txt")
            .pageWasRendered("/sitemap.xml")
            .pageWasRendered("/sitemap-home.xml")
            .pageWasRendered("/sitemap-sitemap.xml")

            .nothingElseRendered()
    }

}