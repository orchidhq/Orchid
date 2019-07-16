package com.eden.orchid.posts

import com.eden.orchid.strikt.nothingRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Posts generator")
class PostsGeneratorTest : OrchidIntegrationTest(PostsModule()) {

    @Test
    @DisplayName("Files, formatted correctly in the `posts` directory, gets rendered correctly without any configuration.")
    fun test01() {
        resource("posts/2018-01-01-post-one.md")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
    }

    @Test
    @DisplayName("Files, formatted correctly in the `posts/{year}` directory, gets rendered correctly without any configuration.")
    fun test02() {
        resource("posts/2018/01-01-post-one.md")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
    }

    @Test
    @DisplayName("Files, formatted correctly in the `posts/{year}/{month}` directory, get rendered correctly without any configuration.")
    fun test03() {
        resource("posts/2018/01/01-post-one.md")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
    }

    @Test
    @DisplayName("Files, formatted correctly in the `posts/{year}/{month}/{day}` directory, get rendered correctly without any configuration.")
    fun test04() {
        resource("posts/2018/01/01/post-one.md")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
    }

    @Test
    @DisplayName("The `permalink` can be set in a post's options.")
    fun test05() {
        resource("posts/2018-01-01-post-one.md", "", mapOf("permalink" to "blog/:year/:month/:slug"))

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/blog/2018/1/post-one/index.html")
    }

    @Test
    @DisplayName("The `permalink` can be set in `defaultOptions`, which is applied to all posts by default.")
    fun test06() {
        configObject("posts", """{"defaultConfig": {"permalink": "blog/:year/:month/:slug"}}""")
        resource("posts/2018-01-01-post-one.md")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/blog/2018/1/post-one/index.html")
    }

    @Test
    @DisplayName("The `permalink` in a post's options overrides the one set in `defaultOptions`.")
    fun test07() {
        configObject("posts", """{"defaultConfig": {"permalink": "defaultConfig/:year/:month/:slug"}}""")
        resource("posts/2018-01-01-post-one.md", "", mapOf("permalink" to "postConfig/:year/:month/:slug"))

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/postConfig/2018/1/post-one/index.html")
    }

    @Test
    @DisplayName("Posts supports multiple categories. Setting a category as a string value uses all category defaults.")
    fun tet08() {
        configObject("posts", """{"categories": ["cat1", "cat2"]}""")
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/cat1/2018/1/1/post-one/index.html")
        expectThat(testResults).pageWasRendered("/cat2/2018/2/2/post-one/index.html")
    }

    @Test
    @DisplayName("Posts supports multiple categories. You can list each category as an Object to customize its options.")
    fun test09() {
        configObject("posts", """{"categories": [{"cat1": {}}, {"cat2": {}}]}""")
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/cat1/2018/1/1/post-one/index.html")
        expectThat(testResults).pageWasRendered("/cat2/2018/2/2/post-one/index.html")
    }

    @Test
    @DisplayName("Posts supports multiple categories. Rather than a list for the categories, you can use a single Object, where each key points to the options for the value, to query easily.")
    fun test10() {
        configObject("posts", """ {"categories": {"cat1": {}, "cat2": {}}}""")
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/cat1/2018/1/1/post-one/index.html")
        expectThat(testResults).pageWasRendered("/cat2/2018/2/2/post-one/index.html")
    }

    @Test
    @DisplayName("Categories can be hierarchical, by using a path rather than just a key.")
    fun test11() {
        configObject("posts", """{"categories": ["cat1", "cat1/cat2"]}""")
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat1/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/cat1/2018/1/1/post-one/index.html")
        expectThat(testResults).pageWasRendered("/cat1/cat2/2018/2/2/post-one/index.html")
    }

    @Test
    @DisplayName("Hierarchical categories must have every category level individually-defined.")
    fun test12() {
        configObject("posts", """{"categories": ["cat1", "cat2/cat3"]}""")
        resource("posts/cat1/2018-01-01-post-one.md")
        resource("posts/cat1/cat2/2018-02-02-post-one.md")

        val testResults = execute()
        expectThat(testResults).nothingRendered()
    }

    @Test
    @DisplayName("Authors can be specified both in post config, and as files in the 'posts/authors/' directory.")
    fun test13() {
        configObject("posts", """{"authors": ["author-one"]}""")
        resource("posts/authors/author-two.md", "", "{}")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/authors/author-one/index.html")
        expectThat(testResults).pageWasRendered("/authors/author-two/index.html")
    }

    @Test
    @DisplayName("Authors can be specified both in post config as objects as well as Strings.")
    fun test14() {
        configObject("posts", """{"authors": ["Author One", {"name": "Author Two", "email": "email@email.com"}]}""")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/authors/author-one/index.html")
        expectThat(testResults).pageWasRendered("/authors/author-two/index.html")
    }

    @Test
    @DisplayName("The Posts generator finishes successfully when there are no resources for it.")
    fun test15() {
        val testResults = execute()
        expectThat(testResults).nothingRendered()
    }

    @Test
    @DisplayName("The Wiki generator finishes successfully when there are no resources for it, when using multiple categories.")
    fun test16() {
        configObject("posts", """{"categories": ["cat1", "cat2/cat3"]}""")
        val testResults = execute()
        expectThat(testResults).nothingRendered()
    }

}
