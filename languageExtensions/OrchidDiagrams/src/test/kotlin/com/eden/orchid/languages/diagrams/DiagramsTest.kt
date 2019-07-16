package com.eden.orchid.languages.diagrams

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtml
import com.eden.orchid.strikt.matchCountIs
import com.eden.orchid.strikt.matches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Tests behavior of using Asciidoc for the homepage")
class DiagramsTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Test that PlantUml works normally")
    fun test01() {
        resource(
            "homepage.md",
            """
            |Bob->Alice : hello
            """.trimMargin()
        )

        val testResults = execute()
        expectThat(testResults)
            .pageWasRendered("/index.html")
            .get { content }
            .asHtml(true)
            .select("body")
            .matches()
            .innerHtml()
            .isEqualTo("<p>Bob-&gt;Alice : hello</p>")
    }

    @Test
    @DisplayName("Test that PlantUml syntax is not supported when the module is not included. Homepage file will not be found at all.")
    fun test02() {
        resource(
            "homepage.uml",
            """
            |Bob->Alice : hello
            """.trimMargin()
        )

        val testResults = execute()
        expectThat(testResults)
            .pageWasRendered("/index.html")
            .get { content }
            .asHtml(true)
            .select("body")
            .matches()
            .innerHtml()
            .isEqualTo("")
    }

    @Test
    @DisplayName("Test that PlantUml syntax works when the file ends with .uml when the module is included")
    fun test03() {
        resource(
            "homepage.uml",
            """
            |Bob->Alice : hello
            """.trimMargin()
        )

        val testResults = execute(DiagramsModule())
        expectThat(testResults)
            .pageWasRendered("/index.svg")
            .get { content }
            .asHtml(true)
            .select("body > svg")
            .matches()
    }

    @Test
    @DisplayName("Test that PlantUml syntax works when the file ends with .puml when the module is included")
    fun test04() {
        resource(
            "homepage.puml",
            """
            |Bob->Alice : hello
            """.trimMargin()
        )

        val testResults = execute(DiagramsModule())
        expectThat(testResults)
            .pageWasRendered("/index.svg")
            .get { content }
            .asHtml(true)
            .select("body > svg")
            .matches()
    }

    @Test
    @DisplayName("Test that PlantUml syntax works with @startuml and @enduml tags")
    fun test05() {
        val input = """
            |@startuml
            |Bob->Alice : hello
            |@enduml
            """.trimMargin()
        val output = PlantUmlCompiler().compile("uml", input, null)
        expectThat(output)
            .asHtml(true)
            .select("svg")
            .matches()
            .matchCountIs(1)
    }

    @Test
    @DisplayName("Test that PlantUml syntax works without @startuml and @enduml tags")
    fun test06() {
        val input = """
            |Bob->Alice : hello
            """.trimMargin()
        val output = PlantUmlCompiler().compile("uml", input, null)
        expectThat(output)
            .asHtml(true)
            .select("svg")
            .matches()
            .matchCountIs(1)
    }

    @Test
    @DisplayName("Test that PlantUml syntax works with multiple @startuml and @enduml tags, rendering each one individually")
    fun test07() {
        val input = """
            |@startuml
            |Bob1->Alice1 : hello
            |@enduml
            |
            |@startuml
            |Bob2->Alice2 : hello
            |@enduml
            |
            |@startuml
            |Bob2->Alice2 : hello
            |@enduml
            """.trimMargin()
        val output = PlantUmlCompiler().compile("uml", input, null)
        expectThat(output)
            .asHtml(true)
            .select("svg")
            .matches()
            .matchCountIs(3)
    }

}
