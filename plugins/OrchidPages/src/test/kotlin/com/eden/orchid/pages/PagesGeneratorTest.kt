package com.eden.orchid.pages

import com.eden.orchid.strikt.nothingRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Pages generator")
class PagesGeneratorTest : OrchidIntegrationTest(PagesModule()) {

    @Test
    @DisplayName("Files in the `pages` directory are rendered directly.")
    fun test01() {
        resource("pages/page-one.md")

        expectThat(execute())
            .pageWasRendered("/page-one/index.html")
    }

    @Test
    @DisplayName("Files in the `pages` directory with a filename of `index` are rendered as that directory's index.")
    fun test02() {
        resource("pages/page-one/index.md")

        expectThat(execute())
            .pageWasRendered("/page-one/index.html")
    }

    @Test
    @DisplayName("You can change the base directory where pages are found.")
    fun test03() {
        configObject("pages", """{"baseDir": "otherPages"}""")
        resource("otherPages/page-one.md")

        expectThat(execute())
            .pageWasRendered("/page-one/index.html")
    }

    @Test
    @DisplayName("When the base directory is changed, pages in default `pages` directory will no longer be used.")
    fun test04() {
        configObject("pages", """{"baseDir": "otherPages"}""")
        resource("pages/page-one.md")

        expectThat(execute())
            .nothingRendered()
    }

    @Test
    @DisplayName("The Pages generator finishes successfully when there are no resources for it.")
    fun test05() {
        expectThat(execute())
            .nothingRendered()
    }

}
