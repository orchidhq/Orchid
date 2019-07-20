package com.eden.orchid.impl.compilers.scss

import com.eden.orchid.impl.generators.AssetsGenerator
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Tests behavior of using SCSS and Sass compilation.")
class ScssTest : OrchidIntegrationTest(withGenerator<AssetsGenerator>()) {

// SCSS
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test that SCSS syntax works normally")
    fun test01() {
        resource(
            "assets/media/test.scss",
            """
            |.a {
            |  .b {
            |    color: blue;
            |  }
            |}
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that a simple SCSS import works")
    fun test02() {
        resource(
            "assets/media/test.scss",
            """
            |@import 'test2';
            |.a {
            |  .b {
            |    color: blue;
            |  }
            |}
            """.trimMargin()
        )

        // all SCSS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.scss",
            """
            |.c {
            |  .d {
            |    color: red;
            |  }
            |}
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that a simple SCSS import works using `partial` filename ")
    fun test03() {
        resource(
            "assets/media/test.scss",
            """
            |@import 'test2';
            |.a {
            |  .b {
            |    color: blue;
            |  }
            |}
            """.trimMargin()
        )

        // all SCSS is imported relative to assets/css/ for now
        resource(
            "assets/css/_test2.scss",
            """
            |.c {
            |  .d {
            |    color: red;
            |  }
            |}
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that a simple SCSS import works adding .scss extension")
    fun test04() {
        resource(
            "assets/media/test.scss",
            """
            |@import 'test2.scss';
            |.a {
            |  .b {
            |    color: blue;
            |  }
            |}
            """.trimMargin()
        )

        // all SCSS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.scss",
            """
            |.c {
            |  .d {
            |    color: red;
            |  }
            |}
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that a simple SCSS import does not work adding .scss extension when only sass exists")
    fun test05() {
        resource(
            "assets/media/test.scss",
            """
            |@import 'test2.scss';
            |.a {
            |  .b {
            |    color: blue;
            |  }
            |}
            """.trimMargin()
        )

        // all SCSS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.sass",
            """
            |.c
            |  .d
            |    color: red;
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            ""
        )
    }

    @Test
    @DisplayName("Test that a simple SCSS import will find and import a sass file if no extension is given")
    fun test06() {
        resource(
            "assets/media/test.scss",
            """
            |@import 'test2';
            |.a {
            |  .b {
            |    color: blue;
            |  }
            |}
            """.trimMargin()
        )

        // all SCSS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.sass",
            """
            |.c
            |  .d
            |    color: red;
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that basic SCSS recursive imports work")
    fun test07() {
        resource(
            "assets/media/test.scss",
            """
            |@import 'test2';
            |.a {
            |  .b {
            |    color: blue;
            |  }
            |}
            """.trimMargin()
        )

        // all SCSS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.scss",
            """
            |@import 'test3';
            |.c {
            |  .d {
            |    color: red;
            |  }
            |}
            """.trimMargin()
        )
        resource(
            "assets/css/test3.scss",
            """
            |@import 'test4';
            |.e {
            |  .f {
            |    color: green;
            |  }
            |}
            """.trimMargin()
        )
        resource(
            "assets/css/test4.scss",
            """
            |.g {
            |  .h {
            |    color: yellow;
            |  }
            |}
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.g .h {
            |  color: yellow; }
            |
            |.e .f {
            |  color: green; }
            |
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName(
        "Test absolute imports. Imports that start with `/` will not be loaded relative to the defailt " +
                "`assets/css` directory or its current location in the import hierarchy, but instead at that exact path " +
                "in the site resources. Relative imports from there follow the new hierarchy."
    )
    fun test08() {
        resource(
            "assets/media/test.scss",
            """
            |@charset "utf-8";
            |@import "test/vars";
            |@import "test/layout/all";
            """.trimMargin()
        )

        resource(
            "assets/css/test/_vars.scss",
            """
                |${'$'}orange:       #E87B2E;
                |${'$'}yellow:       #DBD5AB;
                |${'$'}green:        #8B8F5E;
                |${'$'}turquoise:    #56C2D9;
                |${'$'}cyan:         #6F8787;
                |${'$'}blue:         #7695B2;
                |${'$'}purple:       #625D73;
                |${'$'}red:          #D97E74;
                |
                |${'$'}primary: ${'$'}purple;
                |
                |${'$'}info: ${'$'}blue;
                |${'$'}success: ${'$'}green;
                |${'$'}warning: ${'$'}yellow;
                |${'$'}danger: ${'$'}orange;
                |
                |${'$'}code: ${'$'}blue;
            """.trimMargin()
        )
        resource(
            "assets/css/test/layout/_all.scss",
            """
            |@charset "utf-8";
            |
            |@import "hero.scss";
            |@import "/assets/css/section.scss";
            |@import "/footer.scss";
            |@import "/some/other/dir/import.scss";
            """.trimMargin()
        )
        resource(
            "assets/css/test/layout/_hero.scss",
            """
            |.hero {
            |   display: flex;
            |   color: ${'$'}info;
            |}
            """.trimMargin()
        )
        resource(
            "assets/css/_section.scss",
            """
            |.section {
            |   display: flex;
            |   color: ${'$'}success;
            |}
            """.trimMargin()
        )
        resource(
            "_footer.scss",
            """
            |.footer {
            |   display: flex;
            |   color: ${'$'}warning;
            |}
            """.trimMargin()
        )

        resource(
            "some/other/dir/import.scss",
            """
            |.inner {
            |   color: ${'$'}danger;
            |   @import "deeper/still.scss";
            |}
            """.trimMargin()
        )

        resource(
            "some/other/dir/deeper/still.scss",
            """
            |.still {
            |   color: ${'$'}code;
            |}
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.hero {
            |   display: flex;
            |   color: #7695B2;
            |}
            |.section {
            |   display: flex;
            |   color: #8B8F5E;
            |}
            |.footer {
            |   display: flex;
            |   color: #DBD5AB;
            |}
            |.inner {
            |   color: #E87B2E;
            |}
            |.inner .still {
            |   color: #7695B2;
            |}
            """.trimMargin()
        )
    }

// Sass
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test that SASS syntax works normally")
    fun test11() {
        resource(
            "assets/media/test.sass",
            """
            |.a
            |  .b
            |    color: blue
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that a simple SASS import works")
    fun test12() {
        resource(
            "assets/media/test.sass",
            """
            |@import test2
            |.a
            |  .b
            |    color: blue
            """.trimMargin()
        )

        // all SASS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.sass",
            """
            |.c
            |  .d
            |    color: red
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that a simple SASS import works using `partial` filename ")
    fun test13() {
        resource(
            "assets/media/test.sass",
            """
            |@import test2
            |.a
            |  .b
            |    color: blue
            """.trimMargin()
        )

        // all SASS is imported relative to assets/css/ for now
        resource(
            "assets/css/_test2.sass",
            """
            |.c
            |  .d
            |    color: red
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that a simple SASS import works adding .sass extension")
    fun test14() {
        resource(
            "assets/media/test.sass",
            """
            |@import test2.sass
            |.a
            |  .b
            |    color: blue
            """.trimMargin()
        )

        // all SCSS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.sass",
            """
            |.c
            |  .d
            |    color: red
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that a simple SASS import does not work adding .sass extension when only scss exists")
    fun test15() {
        resource(
            "assets/media/test.sass",
            """
            |@import test2.sass
            |.a
            |  .b
            |    color: blue
            """.trimMargin()
        )

        // all SCSS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.scss",
            """
            |.c {
            |  .d {
            |    color: red;
            |  }
            |}
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            ""
        )
    }

    @Test
    @DisplayName("Test that a simple SASS import will find and import a scss file if no extension is given")
    fun test16() {
        resource(
            "assets/media/test.sass",
            """
            |@import test2
            |.a
            |  .b
            |    color: blue
            """.trimMargin()
        )

        // all SCSS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.scss",
            """
            |.c {
            |  .d {
            |    color: red;
            |  }
            |}
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test that a SASS recursive import imports work")
    fun test17() {
        resource(
            "assets/media/test.sass",
            """
            |@import test2
            |.a
            |  .b
            |    color: blue
            """.trimMargin()
        )

        // all SASS is imported relative to assets/css/ for now
        resource(
            "assets/css/test2.sass",
            """
            |@import test3
            |.c
            |  .d
            |    color: red
            """.trimMargin()
        )
        resource(
            "assets/css/test3.sass",
            """
            |@import test4
            |.e
            |  .f
            |    color: green
            """.trimMargin()
        )
        resource(
            "assets/css/test4.sass",
            """
            |.g
            |  .h
            |    color: yellow
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/test.css",
            """
            |.g .h {
            |  color: yellow; }
            |
            |.e .f {
            |  color: green; }
            |
            |.c .d {
            |  color: red; }
            |
            |.a .b {
            |  color: blue; }
            |
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test import of Bulma's file structure. Currently, variables do not get imported, so this will fail.")
    fun test21() {
        resource(
            "assets/media/bulma.sass",
            """
            |@charset "utf-8"
            |@import "bulma/vars"
            |@import "bulma/layout/all"
            """.trimMargin()
        )

        resource(
            "assets/css/bulma/_vars.sass",
            """
                |${'$'}orange:       #E87B2E
                |${'$'}yellow:       #DBD5AB
                |${'$'}green:        #8B8F5E
                |${'$'}turquoise:    #56C2D9
                |${'$'}cyan:         #6F8787
                |${'$'}blue:         #7695B2
                |${'$'}purple:       #625D73
                |${'$'}red:          #D97E74
                |
                |${'$'}primary: ${'$'}purple
                |
                |${'$'}info: ${'$'}blue
                |${'$'}success: ${'$'}green
                |${'$'}warning: ${'$'}yellow
                |${'$'}danger: ${'$'}orange
                |
                |${'$'}code: ${'$'}blue
            """.trimMargin()
        )
        resource(
            "assets/css/bulma/layout/_all.sass",
            """
            |@charset "utf-8"
            |
            |@import "hero.sass"
            |@import "section.sass"
            |@import "footer.sass"
            """.trimMargin()
        )
        resource(
            "assets/css/bulma/layout/_hero.sass",
            """
            |.hero
            |   display: flex
            |   color: ${'$'}info
            """.trimMargin()
        )
        resource(
            "assets/css/bulma/layout/_section.sass",
            """
            |.section
            |   display: flex
            |   color: ${'$'}success
            """.trimMargin()
        )
        resource(
            "assets/css/bulma/layout/_footer.sass",
            """
            |.footer
            |   display: flex
            |   color: ${'$'}warning
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/bulma.css",
            ""
        )
    }

    @Test
    @DisplayName("Test import of Bulma's file structure. As we are not using variables, this should work fine.")
    fun test22() {
        resource(
            "assets/media/bulma.sass",
            """
            |@charset "utf-8"
            |@import "bulma/vars"
            |@import "bulma/layout/all"
            """.trimMargin()
        )

        resource(
            "assets/css/bulma/_vars.sass",
            """
                |${'$'}orange:       #E87B2E
                |${'$'}yellow:       #DBD5AB
                |${'$'}green:        #8B8F5E
                |${'$'}turquoise:    #56C2D9
                |${'$'}cyan:         #6F8787
                |${'$'}blue:         #7695B2
                |${'$'}purple:       #625D73
                |${'$'}red:          #D97E74
                |
                |${'$'}primary: ${'$'}purple
                |
                |${'$'}info: ${'$'}blue
                |${'$'}success: ${'$'}green
                |${'$'}warning: ${'$'}yellow
                |${'$'}danger: ${'$'}orange
                |
                |${'$'}code: ${'$'}blue
            """.trimMargin()
        )
        resource(
            "assets/css/bulma/layout/_all.sass",
            """
            |@charset "utf-8"
            |
            |@import "hero.sass"
            |@import "section.sass"
            |@import "footer.sass"
            """.trimMargin()
        )
        resource(
            "assets/css/bulma/layout/_hero.sass",
            """
            |.hero
            |   display: flex
            |   color: #7695B2
            """.trimMargin()
        )
        resource(
            "assets/css/bulma/layout/_section.sass",
            """
            |.section
            |   display: flex
            |   color: #8B8F5E
            """.trimMargin()
        )
        resource(
            "assets/css/bulma/layout/_footer.sass",
            """
            |.footer
            |   display: flex
            |   color: #DBD5AB
            """.trimMargin()
        )

        execute().checkAndLog(
            "/assets/media/bulma.css",
            """
            |.hero {
            |  display: flex;
            |  color: #7695B2; }
            |
            |.section {
            |  display: flex;
            |  color: #8B8F5E; }
            |
            |.footer {
            |  display: flex;
            |  color: #DBD5AB; }
            |
            """.trimMargin()
        )
    }

// Check and Log
//----------------------------------------------------------------------------------------------------------------------

    private fun TestResults.checkAndLog(filename: String, expected: String) {
        expectThat(this)
            .pageWasRendered(filename) {
                get { content.replace("\\s".toRegex(), "") }
                    .isEqualTo(expected.replace("\\s".toRegex(), ""))
            }
    }

}
