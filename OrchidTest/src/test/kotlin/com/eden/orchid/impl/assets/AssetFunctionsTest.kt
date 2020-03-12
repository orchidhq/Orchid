package com.eden.orchid.impl.assets

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.img
import kotlinx.html.p
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests behavior of asset functions.")
class AssetFunctionsTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Test asset function works properly")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |![image]({{ "assets/media/image.jpg"|asset }})
            """.trimMargin()
        )

        classpathResource("assets/media/image.jpg")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        img(src = "http://orchid.test/assets/media/image.jpg") {
                            alt = "image"
                        }
                    }
                }
            }
            .pageWasRendered("/assets/media/image.jpg") {}
            .pageWasRendered("/favicon.ico") {}
            .pageWasRendered("/404.html") {}
            .nothingElseRendered()
    }

    @Test
    @DisplayName("Test rename function works properly")
    fun test02() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |![image]({{ "assets/media/image.jpg"|asset|rename('some/other/directory.jpg') }})
            """.trimMargin()
        )

        classpathResource("assets/media/image.jpg")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        img(src = "http://orchid.test/some/other/directory.jpg") {
                            alt = "image"
                        }
                    }
                }
            }
            .pageWasRendered("/some/other/directory.jpg") {}
            .pageWasRendered("/favicon.ico") {}
            .pageWasRendered("/404.html") {}
            .nothingElseRendered()
    }

    @Test
    @DisplayName("Test rotate function works properly")
    fun test03() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |![image]({{ "assets/media/image.jpg"|asset|rotate(90) }})
            """.trimMargin()
        )

        classpathResource("assets/media/image.jpg")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        img(src = "http://orchid.test/assets/media/image_rotate-90.0.jpg") {
                            alt = "image"
                        }
                    }
                }
            }
            .pageWasRendered("/assets/media/image_rotate-90.0.jpg") {}
            .pageWasRendered("/favicon.ico") {}
            .pageWasRendered("/404.html") {}
            .nothingElseRendered()
    }

    @Test
    @DisplayName("Test resize function works properly")
    fun test04() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |![image]({{ "assets/media/image.jpg"|asset|resize(800, 600, "exact") }})
            """.trimMargin()
        )

        classpathResource("assets/media/image.jpg")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        img(src = "http://orchid.test/assets/media/image_800x600_exact.jpg") {
                            alt = "image"
                        }
                    }
                }
            }
            .pageWasRendered("/assets/media/image_800x600_exact.jpg") {}
            .pageWasRendered("/favicon.ico") {}
            .pageWasRendered("/404.html") {}
            .nothingElseRendered()
    }

    @Test
    @DisplayName("Test scale function works properly")
    fun test05() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |![image]({{ "assets/media/image.jpg"|asset|scale(0.85) }})
            """.trimMargin()
        )

        classpathResource("assets/media/image.jpg")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        img(src = "http://orchid.test/assets/media/image_scale-0.85.jpg") {
                            alt = "image"
                        }
                    }
                }
            }
            .pageWasRendered("/assets/media/image_scale-0.85.jpg") {}
            .pageWasRendered("/favicon.ico") {}
            .pageWasRendered("/404.html") {}
            .nothingElseRendered()
    }

    @Test
    @DisplayName("Test image function works properly")
    fun test06() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{{ "assets/media/image.jpg"|img('image') }}
            """.trimMargin()
        )

        classpathResource("assets/media/image.jpg")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        img(src = "http://orchid.test/assets/media/image.jpg") {
                            alt = "image"
                        }
                    }
                }
            }
            .pageWasRendered("/assets/media/image.jpg") {}
            .pageWasRendered("/favicon.ico") {}
            .pageWasRendered("/404.html") {}
            .nothingElseRendered()
    }

    @Test
    @DisplayName("Test scale function works properly when rendered using img function")
    fun test07() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{{ "assets/media/image.jpg"|asset|scale(0.85)|img('image') }}
            """.trimMargin()
        )

        classpathResource("assets/media/image.jpg")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        img(src = "http://orchid.test/assets/media/image_scale-0.85.jpg") {
                            alt = "image"
                        }
                    }
                }
            }
            .pageWasRendered("/assets/media/image_scale-0.85.jpg") {}
            .pageWasRendered("/favicon.ico") {}
            .pageWasRendered("/404.html") {}
            .nothingElseRendered()
    }

}
