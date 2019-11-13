package com.eden.orchid.languages.ink

import com.eden.orchid.impl.generators.AssetsGenerator
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatchesString
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains

class InkTest : OrchidIntegrationTest(InkModule(), withGenerator<HomepageGenerator>()) {

    private val testPageContents = """
            |# pageclearing: false
            |# history: 100
            |
            |LONDON, 1872
            |Residence of Monsieur Phileas Fogg.
            |-> london
            |
            |=== london ===
            |Monsieur Phileas Fogg returned home early from the Reform Club, and in a new-fangled steam-carriage, besides!
            |"Passepartout," said he. "We are going around the world!"
            |
            |+ "Around the world, Monsieur?"
            |    I was utterly astonished.
            |    -> astonished
            |+ [Nod curtly.] -> nod
            |
            |
            |=== astonished ===
            |"You are in jest!" I told him in dignified affront. "You make mock of me, Monsieur."
            |"I am quite serious."
            |
            |+ "But of course"
            |    -> ending
            |
            |
            |=== nod ===
            |I nodded curtly, not believing a word of it.
            |-> ending
            |
            |
            |=== ending
            |"We shall circumnavigate the globe within eighty days." He was quite calm as he proposed this wild scheme. "We leave for Paris on the 8:25. In an hour."
            |-> END
            """.trimMargin()

    @Test
    @DisplayName("Test that the Ink component successfully inlines a story's resource into the page.")
    fun test01() {
        resource(
            "homepage.md",
            "",
            mapOf(
                "components" to listOf(
                    mapOf(
                        "type" to "inkStory",
                        "src" to "story.ink"
                    )
                )
            )
        )
        resource("story.ink", testPageContents)

        expectThat(execute())
            .pageWasRendered("/assets/js/inkle.js") { }
            .pageWasRendered("/assets/js/inkleMain.js") { }
            .pageWasRendered("/assets/css/inkle.css") { }
            .pageWasRendered("/favicon.ico") { }
            .pageWasRendered("/404.html") { }
            .pageWasRendered("/index.html") {
                htmlBodyMatchesString {
                    it
                        .contains("\"inkVersion\":19")                  // Ink successfully rendered
                        .contains("Residence of Monsieur Phileas Fogg") // Our copy exists in the rendered text
                }
            }
            .nothingElseRendered()
    }

    @Test
    @DisplayName("Test that pages with the `.ink` extension get compiled to their proper JSON.")
    fun test02() {
        resource("assets/media/story.ink", testPageContents)

        expectThat(execute(withGenerator<AssetsGenerator>()))
            .pageWasRendered("/favicon.ico") { }
            .pageWasRendered("/404.html") { }
            .pageWasRendered("/index.html") { }
            .pageWasRendered("/assets/media/story.json") {
                get { content }
                    .contains("\"inkVersion\":19")                  // Ink successfully rendered
                    .contains("Residence of Monsieur Phileas Fogg") // Our copy exists in the rendered text
            }
            .nothingElseRendered()
    }
}
