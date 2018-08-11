package com.eden.orchid.taxonomies

import com.eden.orchid.pages.PagesModule
import com.eden.orchid.posts.PostsModule
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.pageWasRendered
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect

@DisplayName("Tests page-rendering behavior of Taxonomy generator, using Posts and Pages as the data source.")
class TaxonomiesGeneratorTest : OrchidIntegrationTest(PostsModule(), PagesModule(), TaxonomiesModule()) {

    @Test
    @DisplayName("Files, formatted correctly in the `wiki` directory, get rendered correctly without any configuration.")
    fun test01() {
        resource("posts/2018-01-01-post-one.md")
        resource("posts/2018-02-01-post-two.md")
        resource("posts/2018-03-01-post-three.md")

        resource("pages/page-one.md")
        resource("pages/page-two.md")
        resource("pages/page-three.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
        expect(testResults).pageWasRendered("/2018/2/1/post-two/index.html")
        expect(testResults).pageWasRendered("/2018/3/1/post-three/index.html")
        expect(testResults).pageWasRendered("/page-one/index.html")
        expect(testResults).pageWasRendered("/page-two/index.html")
        expect(testResults).pageWasRendered("/page-three/index.html")
    }

}
