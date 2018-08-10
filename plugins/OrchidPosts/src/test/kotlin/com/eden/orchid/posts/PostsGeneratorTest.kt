package com.eden.orchid.posts

import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestOrchidRunner
import com.eden.orchid.testhelpers.pageWasRendered
import org.junit.jupiter.api.Test
import strikt.api.expect

class PostsGeneratorTest : OrchidIntegrationTest() {

    @Test
    fun testDefaultPostRendered() {
        val helperClass = TestOrchidRunner()
        val flags = mapOf<String, Any?>()
        val config = mapOf<String, Any?>()
        val resources = mapOf(
                "posts/2018-01-01-post-one.md" to ("Post Content One" to mapOf<String, Any?>())
        )

        val testResults = helperClass.runTest(flags, config, resources, PostsModule())
        expect(testResults).pageWasRendered("/2018/1/1/post-one/index.html")
    }

    @Test
    fun testDefaultPostRenderedWithLocalPermalink() {
        val helperClass = TestOrchidRunner()
        val flags = mapOf<String, Any?>()
        val config = mapOf<String, Any?>()
        val resources = mapOf(
                "posts/2018-01-01-post-one.md" to ("Post Content One" to mapOf(
                        "permalink" to "blog/:year/:month/:slug"
                ))
        )

        val testResults = helperClass.runTest(flags, config, resources, PostsModule())
        expect(testResults).pageWasRendered("/blog/2018/1/post-one/index.html")
    }

    @Test
    fun testDefaultPostRenderedWithGlobalPermalink() {
        val helperClass = TestOrchidRunner()
        val flags = mapOf<String, Any?>()
        val config = mapOf(
                "posts" to mapOf(
                        "defaultConfig" to mapOf(
                                "permalink" to "blog/:year/:month/:slug"
                        )
                )
        )
        val resources = mapOf(
                "posts/2018-01-01-post-one.md" to ("Post Content One" to mapOf<String, Any?>())
        )

        val testResults = helperClass.runTest(flags, config, resources, PostsModule())
        expect(testResults).pageWasRendered("/blog/2018/1/post-one/index.html")
    }

}