package com.eden.orchid.pages

import com.eden.orchid.testhelpers.TestOrchidRunner
import com.eden.orchid.testhelpers.pageWasNotRendered
import com.eden.orchid.testhelpers.pageWasRendered
import org.junit.jupiter.api.Test
import strikt.api.expect

class PagesGeneratorTest {

    @Test
    fun testNormalPageRendered() {
        val helperClass = TestOrchidRunner()
        val flags = mapOf<String, Any?>()
        val config = mapOf<String, Any?>()
        val resources = mapOf(
                "pages/page-one.md" to ("Page Content One" to mapOf<String, Any?>())
        )

        val testResults = helperClass.runTest(flags, config, resources, PagesModule())
        expect(testResults).pageWasRendered("/page-one/index.html")
    }

    @Test
    fun testIndexPageRendered() {
        val helperClass = TestOrchidRunner()
        val flags = mapOf<String, Any?>()
        val config = mapOf<String, Any?>()
        val resources = mapOf(
                "pages/page-one/index.md" to ("Page Content One" to mapOf<String, Any?>())
        )

        val testResults = helperClass.runTest(flags, config, resources, PagesModule())
        expect(testResults).pageWasRendered("/page-one/index.html")
    }

    @Test
    fun testPagesDirNotRenderedWhenUsingOtherBaseDir() {
        val helperClass = TestOrchidRunner()
        val flags = mapOf<String, Any?>()
        val config = mapOf(
                "pages" to mapOf(
                        "baseDir" to "otherPages"
                )
        )
        val resources = mapOf(
                "pages/page-one/index.md" to ("Page Content One" to mapOf<String, Any?>())
        )

        val testResults = helperClass.runTest(flags, config, resources, PagesModule())
        expect(testResults).pageWasNotRendered("/page-one/index.html")
    }

    @Test
    fun testUsingOtherBaseDir() {
        val helperClass = TestOrchidRunner()
        val flags = mapOf<String, Any?>()
        val config = mapOf(
                "pages" to mapOf(
                        "baseDir" to "otherPages"
                )
        )
        val resources = mapOf(
                "otherPages/page-one/index.md" to ("Page Content One" to mapOf<String, Any?>())
        )

        val testResults = helperClass.runTest(flags, config, resources, PagesModule())
        expect(testResults).pageWasRendered("/page-one/index.html")
    }

}