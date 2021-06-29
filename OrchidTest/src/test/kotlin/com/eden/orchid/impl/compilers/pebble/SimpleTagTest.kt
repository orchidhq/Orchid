package com.eden.orchid.impl.compilers.pebble

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.utilities.addToSet
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import strikt.api.expectThat

class SimpleTagTest : OrchidIntegrationTest(SimpleTagModule(), withGenerator<HomepageGenerator>()) {

    @ParameterizedTest
    @MethodSource("params")
    fun testSimpleTags(input: String, expected: String) {
        resource("homepage.peb", input.trim())
        resource("templates/tags/hello.peb", "hello {{ tag.greeting }} {{ tag.closing }}")
        resource("templates/tags/althello.peb", "alt {{ tag.greeting }} {{ tag.closing }}")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    +expected.trim()
                }
            }
    }

    companion object {

        @JvmStatic
        fun params() = sequence<Arguments> {
            // simple tag, no content
            yield(
                Arguments.of(
                    "{% hello %}",
                    "hello world"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' %}",
                    "hello sir"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' 'and goodbye!' %}",
                    "hello sir and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' closing='and goodbye!' %}",
                    "hello sir and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello greeting='sir' %}",
                    "hello sir"
                )
            )
            yield(
                Arguments.of(
                    "{% hello greeting='sir' closing='and goodbye!' %}",
                    "hello sir and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello template = 'althello' %}",
                    "alt world"
                )
            )
        }.asIterable()
    }
}

class SimpleHelloTag : TemplateTag("hello", Type.Simple, true) {

    @Option
    @StringDefault("world")
    lateinit var greeting: String

    @Option
    lateinit var closing: String

    override fun parameters() = arrayOf(::greeting.name, ::closing.name)
}

@IgnoreModule
private class SimpleTagModule : OrchidModule() {
    override fun configure() {
        addToSet<TemplateTag, SimpleHelloTag>()
    }
}
