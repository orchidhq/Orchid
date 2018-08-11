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
    @DisplayName("Taxonomies creates archive pages based on the pages from other generators. Setting taxonomies as a string value uses all category defaults.")
    fun test01() {
        configObject("taxonomies", """{"taxonomies": ["tags"]}""")

        resource("posts/2018-01-01-post-one.md", "", """{"tags": ["tag1"]}""")
        resource("pages/page-one.md", "", """{"tags": ["tag1"]}""")

        val testResults = execute()
        expect(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
        expect(testResults).pageWasRendered("/page-one/index.html")
        expect(testResults).pageWasRendered("/tags/index.html")
        expect(testResults).pageWasRendered("/tags/tag1/index.html")
    }

    @Test
    @DisplayName("Taxonomies creates archive pages based on the pages from other generators. You can list each category as an Object to customize its options.")
    fun test02() {
        configObject("taxonomies", """{"taxonomies": [{"tags": {}}]}""")

        resource("posts/2018-01-01-post-one.md", "", """{"tags": ["tag1"]}""")
        resource("pages/page-one.md", "", """{"tags": ["tag1"]}""")

        val testResults = execute()
        expect(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
        expect(testResults).pageWasRendered("/page-one/index.html")
        expect(testResults).pageWasRendered("/tags/index.html")
        expect(testResults).pageWasRendered("/tags/tag1/index.html")
    }

    @Test
    @DisplayName("Taxonomies creates archive pages based on the pages from other generators. Rather than a list for the categories, you can use a single Object, where each key points to the options for the value, to query easily.")
    fun test03() {
        configObject("taxonomies", """{"taxonomies": {"tags": {}}}""")

        resource("posts/2018-01-01-post-one.md", "", """{"tags": ["tag1"]}""")
        resource("pages/page-one.md", "", """{"tags": ["tag1"]}""")

        val testResults = execute()
        expect(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
        expect(testResults).pageWasRendered("/page-one/index.html")
        expect(testResults).pageWasRendered("/tags/index.html")
        expect(testResults).pageWasRendered("/tags/tag1/index.html")
    }

    @Test
    @DisplayName("Values for taxonomy terms can be gotten through page configs, or implicitly from static data set up by the page's generator.")
    fun test04() {
        configObject("posts", """{"categories": ["category1", "category2"]}""")
        configObject("taxonomies", """{"taxonomies": ["tags", "categories"]}""")

        resource("posts/category1/2018-01-01-post-one.md", "", """{"tags": ["tag1"]}""")
        resource("posts/category2/2018-02-01-post-two.md", "", """{"tags": ["tag1"]}""")
        resource("pages/page-one.md", "", """{"tags": ["tag2"]}""")

        val testResults = execute()
        println(testResults.showResults())
        expect(testResults).pageWasRendered("/category1/2018/1/1/post-one/index.html")
        expect(testResults).pageWasRendered("/category2/2018/2/1/post-two/index.html")
        expect(testResults).pageWasRendered("/page-one/index.html")
        expect(testResults).pageWasRendered("/tags/index.html")
        expect(testResults).pageWasRendered("/tags/tag1/index.html")
        expect(testResults).pageWasRendered("/tags/tag2/index.html")
        expect(testResults).pageWasRendered("/categories/index.html")
        expect(testResults).pageWasRendered("/categories/category1/index.html")
        expect(testResults).pageWasRendered("/categories/category2/index.html")
    }

}
