package com.eden.orchid.swagger

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.matches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.hasSize

@DisplayName("Tests page-rendering behavior of Posts generator")
class SwaggerTest : OrchidIntegrationTest(SwaggerModule(), withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Tests that presentations are set up correctly based on Markdown files in the `presentations` directory.")
    fun test01() {
        resource(
            "homepage.md",
            "",
            """
            |{
            |  "components": [
            |    {
            |      "type": "swaggerUi",
            |       "openApiSource": "https://petstore.swagger.io/v2/swagger.json"
            |    }
            |  ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .and {
                        asHtml()
                            .select("#swagger-ui") {
                                matches()
                                    .hasSize(1)
                            }
                    }
                    .and {
                        contains("https://petstore.swagger.io/v2/swagger.json")
                    }
            }
    }

}