package com.eden.orchid.posts

import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Tests that pages are correctly configured with its archetypes")
class PostConfigurationsTest : OrchidIntegrationTest(PostsModule()) {

    @BeforeEach
    fun setUp() {
        resource("templates/layouts/layoutone.peb")
        resource("templates/layouts/layouttwo.peb")
        resource("templates/layouts/layoutthree.peb")
        resource("templates/layouts/layoutfour.peb")
    }

    @Test
    @DisplayName("Test AllPages archetype is applied")
    fun test01() {
        configObject("allPages", """{"layout": "layoutone"}""")
        resource("posts/2018-12-01-post-one.md")
        expectThat(execute())
                .pageWasRendered("/2018/12/1/post-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutone")
    }

    @Test
    @DisplayName("Test PostPages archetype is applied")
    fun test02() {
        configObject("posts", """{"postPages": {"layout": "layoutone"}}""")
        resource("posts/2018-12-01-post-one.md")
        expectThat(execute())
                .pageWasRendered("/2018/12/1/post-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutone")
    }

    @Test
    @DisplayName("Test PostCategory archetype is applied")
    fun test03() {
        configObject("posts", """{"categories": ["cat1"]}""")
        configObject("posts", """{"cat1": {"layout": "layoutone"}}""")
        resource("posts/cat1/2018-12-01-post-one.md")
        expectThat(execute())
                .pageWasRendered("/cat1/2018/12/1/post-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutone")
    }

    @Test
    @DisplayName("Test StaticPages archetype takes precedence over AllPages")
    fun test04() {
        configObject("allPages", """{"layout": "layoutone"}""")
        configObject("posts", """{"postPages": {"layout": "layouttwo"}}""")

        resource("posts/2018-12-01-post-one.md")
        expectThat(execute())
                .pageWasRendered("/2018/12/1/post-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layouttwo")
    }

    @Test
    @DisplayName("Test PageGroup archetype takes precedence over StaticPages")
    fun test05() {
        configObject("posts", """{"categories": ["cat1"]}""")
        configObject("posts", """{"postPages": {"layout": "layoutone"}}""")
        configObject("posts", """{"cat1": {"layout": "layouttwo"}}""")

        resource("posts/cat1/2018-12-01-post-one.md")
        expectThat(execute())
                .pageWasRendered("/cat1/2018/12/1/post-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layouttwo")
    }

    @Test
    @DisplayName("Test PageGroup, StaticPages, and AllPages archetypes are all applied at the same time")
    fun test06() {
        configObject("posts", """{"categories": ["cat1", "cat2"]}""")
        configObject("allPages", """{"layout": "layoutone"}""")
        configObject("posts", """{"postPages": {"layout": "layouttwo"}}""")
        configObject("posts", """{"cat1": {"layout": "layoutthree"}}""")
        configObject("posts", """{"cat2": {"layout": "layoutfour"}}""")

        resource("posts/cat1/2018-12-01-post-one.md")
        resource("posts/cat2/2018-12-01-post-one.md")

        val testResults = execute()

        expectThat(testResults)
                .pageWasRendered("/cat1/2018/12/1/post-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutthree")

        expectThat(testResults)
                .pageWasRendered("/cat2/2018/12/1/post-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutfour")
    }


}
