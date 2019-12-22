package com.eden.orchid.changelog

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.summary
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.a
import kotlinx.html.details
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.ul
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isTrue

class ChangelogGeneratorTest : OrchidIntegrationTest(ChangelogModule(), withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Test Changelog Generator using default settings (directory-based)")
    fun test01() {
        resource(
            "homepage.md",
            "",
            """
            |{
            |    "components": [
            |        {"type": "changelog"} 
            |    ]
            |}
            """.trimMargin()
        )
        resource(
            "changelog/0.1.0.md",
            "# 0.1.0"
        )
        resource(
            "changelog/0.2.0.md",
            "# 0.2.0"
        )
        resource(
            "changelog/0.3.0.md",
            "# 0.3.0"
        )
        resource(
            "changelog/1.0.0.md",
            "# 1.0.0",
            """
            |{
            |    "releaseDate": "2019-10-1"
            |}
            """.trimMargin()
        )
        resource(
            "changelog/1.1.0.md",
            "# 1.1.0",
            """
            |{
            |    "releaseDate": "2019-11-1"
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-changelog component-order-0") {
                        details {
                            id = "1.1.0"
                            open = true
                            summary { +"1.1.0 - 2019-11-1" }

                            h1 {
                                id = "110"
                                a(href = "#110", classes = "anchor") { id = "110" }
                                +"1.1.0"
                            }
                        }

                        details {
                            id = "1.0.0"
                            summary { +"1.0.0 - 2019-10-1" }

                            h1 {
                                id = "100"
                                a(href = "#100", classes = "anchor") { id = "100" }
                                +"1.0.0"
                            }
                        }
                        details {
                            id = "0.3.0"
                            summary { +"0.3.0" }

                            h1 {
                                id = "030"
                                a(href = "#030", classes = "anchor") { id = "030" }
                                +"0.3.0"
                            }
                        }
                        details {
                            id = "0.2.0"
                            summary { +"0.2.0" }

                            h1 {
                                id = "020"
                                a(href = "#020", classes = "anchor") { id = "020" }
                                +"0.2.0"
                            }
                        }
                        details {
                            id = "0.1.0"
                            summary { +"0.1.0" }

                            h1 {
                                id = "010"
                                a(href = "#010", classes = "anchor") { id = "010" }
                                +"0.1.0"
                            }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test Changelog Generator using customizing Generator with different base directory (directory-based)")
    fun test02() {
        configObject(
            "changelog",
            """
            |{
            |    "baseDir": "otherChangelogDir"
            |}
            """.trimMargin()
        )
        resource(
            "homepage.md",
            "",
            """
            |{
            |    "components": [ 
            |        {"type": "changelog"} 
            |    ]
            |}
            """.trimMargin()
        )
        resource(
            "otherChangelogDir/0.1.0.md",
            "# 0.1.0"
        )
        resource(
            "otherChangelogDir/0.2.0.md",
            "# 0.2.0"
        )
        resource(
            "otherChangelogDir/0.3.0.md",
            "# 0.3.0"
        )
        resource(
            "otherChangelogDir/1.0.0.md",
            "# 1.0.0"
        )
        resource(
            "otherChangelogDir/1.1.0.md",
            "# 1.1.0"
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-changelog component-order-0") {
                        details {
                            id = "1.1.0"
                            open = true
                            summary { +"1.1.0" }

                            h1 {
                                id = "110"
                                a(href = "#110", classes = "anchor") { id = "110" }
                                +"1.1.0"
                            }
                        }

                        details {
                            id = "1.0.0"
                            summary { +"1.0.0" }

                            h1 {
                                id = "100"
                                a(href = "#100", classes = "anchor") { id = "100" }
                                +"1.0.0"
                            }
                        }
                        details {
                            id = "0.3.0"
                            summary { +"0.3.0" }

                            h1 {
                                id = "030"
                                a(href = "#030", classes = "anchor") { id = "030" }
                                +"0.3.0"
                            }
                        }
                        details {
                            id = "0.2.0"
                            summary { +"0.2.0" }

                            h1 {
                                id = "020"
                                a(href = "#020", classes = "anchor") { id = "020" }
                                +"0.2.0"
                            }
                        }
                        details {
                            id = "0.1.0"
                            summary { +"0.1.0" }

                            h1 {
                                id = "010"
                                a(href = "#010", classes = "anchor") { id = "010" }
                                +"0.1.0"
                            }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test Changelog Generator customizing Adapter with different base directory (directory-based)")
    fun test03() {
        configObject(
            "changelog",
            """
            |{
            |    "adapter": {
            |        "type": "directory",
            |        "baseDir": "otherChangelogDir"
            |    }
            |}
            """.trimMargin()
        )
        resource(
            "homepage.md",
            "",
            """
            |{
            |    "components": [ 
            |        {"type": "changelog"} 
            |    ]
            |}
            """.trimMargin()
        )
        resource(
            "otherChangelogDir/0.1.0.md",
            """
            |# 0.1.0
            |
            |- Initial release
            """.trimMargin()
        )
        resource(
            "otherChangelogDir/0.2.0.md",
            "# 0.2.0"
        )
        resource(
            "otherChangelogDir/0.3.0.md",
            "# 0.3.0"
        )
        resource(
            "otherChangelogDir/1.0.0.md",
            "# 1.0.0"
        )
        resource(
            "otherChangelogDir/1.1.0.md",
            "# 1.1.0"
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-changelog component-order-0") {
                        details {
                            id = "1.1.0"
                            open = true
                            summary { +"1.1.0" }

                            h1 {
                                id = "110"
                                a(href = "#110", classes = "anchor") { id = "110" }
                                +"1.1.0"
                            }
                        }

                        details {
                            id = "1.0.0"
                            summary { +"1.0.0" }

                            h1 {
                                id = "100"
                                a(href = "#100", classes = "anchor") { id = "100" }
                                +"1.0.0"
                            }
                        }
                        details {
                            id = "0.3.0"
                            summary { +"0.3.0" }

                            h1 {
                                id = "030"
                                a(href = "#030", classes = "anchor") { id = "030" }
                                +"0.3.0"
                            }
                        }
                        details {
                            id = "0.2.0"
                            summary { +"0.2.0" }

                            h1 {
                                id = "020"
                                a(href = "#020", classes = "anchor") { id = "020" }
                                +"0.2.0"
                            }
                        }
                        details {
                            id = "0.1.0"
                            summary { +"0.1.0" }

                            h1 {
                                id = "010"
                                a(href = "#010", classes = "anchor") { id = "010" }
                                +"0.1.0"
                            }
                            ul {
                                li {
                                    +"Initial release"
                                }
                            }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test Changelog Generator using default settings (file-based, from resources)")
    fun test04() {
        configObject(
            "changelog",
            """
            |{
            |    "adapter": {
            |        "type": "file"
            |    }
            |}
            """.trimMargin()
        )
        resource(
            "homepage.md",
            "",
            """
            |{
            |    "components": [ 
            |        {"type": "changelog"} 
            |    ]
            |}
            """.trimMargin()
        )
        resource(
            "CHANGELOG.md",
            """
            |# 0.1.0
            |
            |- Initial release (from resources)
            |
            |# 0.2.0
            |# 0.3.0
            |# 1.0.0 / 2019-10-1
            |# 1.1.0 - 2019-11-1
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-changelog component-order-0") {
                        details {
                            id = "1.1.0"
                            open = true
                            summary { +"1.1.0 - 2019-11-1" }
                        }
                        details {
                            id = "1.0.0"
                            summary { +"1.0.0 - 2019-10-1" }
                        }
                        details {
                            id = "0.3.0"
                            summary { +"0.3.0" }
                        }
                        details {
                            id = "0.2.0"
                            summary { +"0.2.0" }
                        }
                        details {
                            id = "0.1.0"
                            summary { +"0.1.0" }
                            ul {
                                li {
                                    +"Initial release (from resources)"
                                }
                            }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test Changelog Generator using default settings (file-based, from file)")
    fun test05() {
        configObject(
            "changelog",
            """
            |{
            |    "adapter": {
            |        "type": "file",
            |        "baseDir": "./src/test/resources",
            |        "filename": "CHANGES"
            |    }
            |}
            """.trimMargin()
        )
        resource(
            "homepage.md",
            "",
            """
            |{
            |    "components": [ 
            |        {"type": "changelog"} 
            |    ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-changelog component-order-0") {
                        details {
                            id = "1.1.0"
                            open = true
                            summary { +"1.1.0 - 2019-11-1" }
                        }
                        details {
                            id = "1.0.0"
                            summary { +"1.0.0 - 2019-10-1" }
                        }
                        details {
                            id = "0.3.0"
                            summary { +"0.3.0" }
                        }
                        details {
                            id = "0.2.0"
                            summary { +"0.2.0" }
                        }
                        details {
                            id = "0.1.0"
                            summary { +"0.1.0" }
                            ul {
                                li {
                                    +"Initial release (from file)"
                                }
                            }
                        }
                    }
                }
            }
    }
}
