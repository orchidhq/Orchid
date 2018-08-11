package com.eden.orchid.posts

import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.nothingRendered
import com.eden.orchid.testhelpers.pageWasRendered
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect

@DisplayName("Tests page-rendering behavior of Posts generator")
class PostsGeneratorTest : OrchidIntegrationTest(PostsModule()) {

    @Test
    @DisplayName("Files, formatted correctly in the `posts` directory, gets rendered correctly without any configuration.")
    fun test01() {
        resource("posts/2018-01-01-post-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
    }

    @Test
    @DisplayName("Files, formatted correctly in the `posts/{year}` directory, gets rendered correctly without any configuration.")
    fun test02() {
        resource("posts/2018/01-01-post-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
    }

    @Test
    @DisplayName("Files, formatted correctly in the `posts/{year}/{month}` directory, get rendered correctly without any configuration.")
    fun test03() {
        resource("posts/2018/01/01-post-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
    }

    @Test
    @DisplayName("Files, formatted correctly in the `posts/{year}/{month}` directory, get rendered correctly without any configuration.")
    fun test04() {
        resource("posts/2018/01/01/post-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
    }

    @Test
    @DisplayName("The `permalink` can be set in a post's options.")
    fun test05() {
        resource("posts/2018-01-01-post-one.md", "", mapOf("permalink" to "blog/:year/:month/:slug"))

        val testResults = execute()
        expect(testResults).pageWasRendered("/blog/2018/1/post-one/index.html")
    }

    @Test
    @DisplayName("The `permalink` can be set in `defaultOptions`, which is applied to all posts by default.")
    fun test06() {
        config("posts", mapOf(
                "defaultConfig" to mapOf("permalink" to "blog/:year/:month/:slug")
        ))
        resource("posts/2018-01-01-post-one.md")

        val testResults = execute()
        println(testResults.showResults())
        expect(testResults).pageWasRendered("/blog/2018/1/post-one/index.html")
    }

    @Test
    @DisplayName("The `permalink` in a post's options overrides the one set in `defaultOptions`.")
    fun test07() {
        config("posts", mapOf(
                "defaultConfig" to mapOf("permalink" to "defaultConfig/:year/:month/:slug")
        ))
        resource("posts/2018-01-01-post-one.md", "", mapOf("permalink" to "postConfig/:year/:month/:slug"))

        val testResults = execute()
        expect(testResults).pageWasRendered("/postConfig/2018/1/post-one/index.html")
    }

    @Test
    @DisplayName("Posts supports multiple categories. Setting a category as a string value uses all category defaults.")
    fun tet08() {
        config("posts", mapOf(
                "categories" to listOf("cat1", "cat2")
        ))
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/cat1/2018/1/1/post-one/index.html")
        expect(testResults).pageWasRendered("/cat2/2018/2/2/post-one/index.html")
    }

    @Test
    @DisplayName("Posts supports multiple categories. You can list each category as an Object to customize its options.")
    fun test09() {
        config("posts", mapOf(
                "categories" to listOf(
                        mapOf("cat1" to mapOf<String, Any?>()),
                        mapOf("cat2" to mapOf<String, Any?>())
                )
        ))
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/cat1/2018/1/1/post-one/index.html")
        expect(testResults).pageWasRendered("/cat2/2018/2/2/post-one/index.html")
    }

    @Test
    @DisplayName("Posts supports multiple categories. Rather than a list for the categories, you can use a single Object, where each key points to the options for the value, to query easily.")
    fun test10() {
        config("posts", mapOf(
                "categories" to mapOf(
                        "cat1" to mapOf<String, Any?>(),
                        "cat2" to mapOf<String, Any?>()
                )
        ))
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/cat1/2018/1/1/post-one/index.html")
        expect(testResults).pageWasRendered("/cat2/2018/2/2/post-one/index.html")
    }

    @Test
    @DisplayName("Categories can be hierarchical, by using a path rather than just a key.")
    fun test11() {
        config("posts", mapOf(
                "categories" to listOf(
                        "cat1",
                        "cat1/cat2"
                )
        ))
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat1/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/cat1/2018/1/1/post-one/index.html")
        expect(testResults).pageWasRendered("/cat1/cat2/2018/2/2/post-one/index.html")
    }

    @Test
    @DisplayName("Hierarchical categories must have every category level individually-defined.")
    fun test12() {
        config("posts", mapOf(
                "categories" to listOf(
                        "cat1",
                        "cat2/cat3"
                )
        ))
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat1/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expect(testResults).nothingRendered()
    }

}
