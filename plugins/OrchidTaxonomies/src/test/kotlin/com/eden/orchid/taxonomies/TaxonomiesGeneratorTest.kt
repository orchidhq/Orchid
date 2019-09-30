package com.eden.orchid.taxonomies

import com.eden.orchid.pages.PagesModule
import com.eden.orchid.posts.PostsModule
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.nothingRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import kotlinx.html.a
import kotlinx.html.li
import kotlinx.html.ul
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Taxonomy generator, using Posts and Pages as the data source.")
class TaxonomiesGeneratorTest : OrchidIntegrationTest(PostsModule(), PagesModule(), TaxonomiesModule()) {

    @Test
    @DisplayName(
        "Taxonomies creates archive pages based on the pages from other generators. Setting taxonomies as a " +
                "string value uses all category defaults."
    )
    fun test01() {
        configObject("taxonomies", """{"taxonomies": ["tags"]}""")

        resource("posts/2018-01-01-post-one.md", "", """{"tags": ["tag1"]}""")
        resource("pages/page-one.md", "", """{"tags": ["tag1"]}""")

        expectThat(execute())
            .pageWasRendered("/2018/1/1/post-one/index.html")
            .pageWasRendered("/page-one/index.html")
            .pageWasRendered("/tags/index.html")
            .pageWasRendered("/tags/tag1/index.html")
            .pageWasRendered("/atom.xml")
            .pageWasRendered("/rss.xml")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

    @Test
    @DisplayName(
        "Taxonomies creates archive pages based on the pages from other generators. You can list each " +
                "category as an Object to customize its options."
    )
    fun test02() {
        configObject("taxonomies", """{"taxonomies": [{"tags": {}}]}""")

        resource("posts/2018-01-01-post-one.md", "", """{"tags": ["tag1"]}""")
        resource("pages/page-one.md", "", """{"tags": ["tag1"]}""")

        expectThat(execute())
            .pageWasRendered("/2018/1/1/post-one/index.html")
            .pageWasRendered("/page-one/index.html")
            .pageWasRendered("/tags/index.html")
            .pageWasRendered("/tags/tag1/index.html")
            .pageWasRendered("/atom.xml")
            .pageWasRendered("/rss.xml")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

    @Test
    @DisplayName(
        "Taxonomies creates archive pages based on the pages from other generators. Rather than a list for " +
                "the categories, you can use a single Object, where each key points to the options for the value, to " +
                "query easily."
    )
    fun test03() {
        configObject("taxonomies", """{"taxonomies": {"tags": {}}}""")

        resource("posts/2018-01-01-post-one.md", "", """{"tags": ["tag1"]}""")
        resource("pages/page-one.md", "", """{"tags": ["tag1"]}""")

        expectThat(execute())
            .pageWasRendered("/2018/1/1/post-one/index.html")
            .pageWasRendered("/page-one/index.html")
            .pageWasRendered("/tags/index.html")
            .pageWasRendered("/tags/tag1/index.html")
            .pageWasRendered("/atom.xml")
            .pageWasRendered("/rss.xml")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

    @Test
    @DisplayName(
        "Values for taxonomy terms can be gotten through page configs, or implicitly from static data set " +
                "up by the page's generator."
    )
    fun test04() {
        configObject("posts", """{"categories": ["category1", "category2"]}""")
        configObject("taxonomies", """{"taxonomies": ["tags", "categories"]}""")

        resource("posts/category1/2018-01-01-post-one.md", "", """{"tags": ["tag1"]}""")
        resource("posts/category2/2018-02-01-post-two.md", "", """{"tags": ["tag1"]}""")
        resource("pages/page-one.md", "", """{"tags": ["tag2"]}""")

        expectThat(execute())
            .pageWasRendered("/category1/2018/1/1/post-one/index.html")
            .pageWasRendered("/category2/2018/2/1/post-two/index.html")
            .pageWasRendered("/page-one/index.html")
            .pageWasRendered("/tags/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/tags/tag2") { +"Tag 2 (1)" } }
                                li { a(href = "http://orchid.test/tags/tag1") { +"Tag 1 (2)" } }
                            }
                        }
                    }
            }
            .pageWasRendered("/tags/tag1/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/category2/2018/2/1/post-two") { +"Post Two" } }
                                li { a(href = "http://orchid.test/category1/2018/1/1/post-one") { +"Post One" } }
                            }
                        }
                    }
            }
            .pageWasRendered("/tags/tag2/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/page-one") { +"Page One" } }
                            }
                        }
                    }
            }
            .pageWasRendered("/categories/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/categories/category2") { +"Category 2 (1)" } }
                                li { a(href = "http://orchid.test/categories/category1") { +"Category 1 (1)" } }
                            }
                        }
                    }
            }
            .pageWasRendered("/categories/category1/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/category1/2018/1/1/post-one") { +"Post One" } }
                            }
                        }
                    }
            }
            .pageWasRendered("/categories/category2/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/category2/2018/2/1/post-two") { +"Post Two" } }
                            }
                        }
                    }
            }
            .pageWasRendered("/atom.xml")
            .pageWasRendered("/rss.xml")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

    @Test
    @DisplayName("The Taxonomies generator finishes successfully when there are no resources for it.")
    fun test05() {
        expectThat(execute())
            .nothingRendered()
    }

    @Test
    @DisplayName(
        "The Taxonomies generator finishes successfully when there are no resources for it, when using " +
                "multiple categories."
    )
    fun test06() {
        configObject("posts", """{"categories": ["category1", "category2"]}""")
        configObject("taxonomies", """{"taxonomies": ["tags", "categories"]}""")

        expectThat(execute())
            .nothingRendered()
    }

    @Test
    @DisplayName("Taxonomies can create simple archive pages from pre-existing collections.")
    fun test07() {
        configObject("taxonomies", """{"collectionArchives": [{"collectionType": "posts", "collectionId": ""}]}""")

        resource("posts/2018-01-01-post-one.md")
        resource("posts/2018-02-01-post-two.md")
        resource("posts/2018-03-01-post-three.md")

        expectThat(execute())
            .pageWasRendered("/atom.xml")
            .pageWasRendered("/rss.xml")
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/2018/1/1/post-one/index.html")
            .pageWasRendered("/2018/2/1/post-two/index.html")
            .pageWasRendered("/2018/3/1/post-three/index.html")
            .pageWasRendered("/posts/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/2018/3/1/post-three") { +"Post Three" } }
                                li { a(href = "http://orchid.test/2018/2/1/post-two") { +"Post Two" } }
                                li { a(href = "http://orchid.test/2018/1/1/post-one") { +"Post One" } }
                            }
                        }
                    }
            }
            .nothingElseRendered()
    }

    @Test
    @DisplayName("Many disparate collections can be merged into the same collection archive.")
    fun test08() {
        enableLogging()
        configObject(
            "taxonomies", """
            |{
            |   "collectionArchives": [
            |       {
            |           "key": "archives",
            |           "merge": [
            |               {"collectionType": "posts", "collectionId": ""},
            |               {"collectionType": "pages", "collectionId": ""}
            |           ]
            |       }
            |   ]
            |}
            |""".trimMargin()
        )

        resource("posts/2018-01-01-post-one.md")
        resource("posts/2018-02-01-post-two.md")
        resource("posts/2018-03-01-post-three.md")
        resource("pages/page-one.md")
        resource("pages/page-two.md")
        resource("pages/page-three.md")

        expectThat(execute())
            .pageWasRendered("/atom.xml")
            .pageWasRendered("/rss.xml")
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/page-one/index.html")
            .pageWasRendered("/page-two/index.html")
            .pageWasRendered("/page-three/index.html")
            .pageWasRendered("/2018/1/1/post-one/index.html")
            .pageWasRendered("/2018/2/1/post-two/index.html")
            .pageWasRendered("/2018/3/1/post-three/index.html")
            .pageWasRendered("/archives/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/page-two") { +"Page Two" } }
                                li { a(href = "http://orchid.test/page-three") { +"Page Three" } }
                                li { a(href = "http://orchid.test/page-one") { +"Page One" } }
                                li { a(href = "http://orchid.test/2018/3/1/post-three") { +"Post Three" } }
                                li { a(href = "http://orchid.test/2018/2/1/post-two") { +"Post Two" } }
                                li { a(href = "http://orchid.test/2018/1/1/post-one") { +"Post One" } }
                            }
                        }
                    }
            }
            .nothingElseRendered()
    }

}
