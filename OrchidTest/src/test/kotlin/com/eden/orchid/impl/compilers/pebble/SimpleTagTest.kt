package com.eden.orchid.impl.compilers.pebble

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtml
import com.eden.orchid.strikt.matches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.utilities.addToSet
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SimpleTagTest : OrchidIntegrationTest(SimpleTagModule(), withGenerator<HomepageGenerator>()) {

    @ParameterizedTest
    @CsvSource(
        "{% hello %}, hello world",
        "{% hello 'sir' %}, hello sir",
        "{% hello greeting='sir' %}, hello sir"
    )
    fun testSimpleTags(input: String, expected: String) {
        resource("homepage.peb", input.trim())
        resource("templates/tags/hello.peb", "hello {{ tag.greeting }}")

        val testResults = execute()
        expectThat(testResults)
            .pageWasRendered("/index.html")
            .get { content }
            .asHtml(true)
            .select("body")
            .matches()
            .innerHtml()
            .isEqualTo(expected.trim())
    }
}

class SimpleHelloTag : TemplateTag("hello", Type.Simple, true) {

    @Option
    @StringDefault("world")
    lateinit var greeting: String

    override fun parameters() = arrayOf("greeting")

}

@IgnoreModule
private class SimpleTagModule : OrchidModule() {
    override fun configure() {
        addToSet<TemplateTag, SimpleHelloTag>()
    }
}