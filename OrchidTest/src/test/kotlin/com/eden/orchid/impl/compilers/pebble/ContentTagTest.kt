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

class ContentTagTest : OrchidIntegrationTest(ContentTagModule(), withGenerator<HomepageGenerator>()) {

    @ParameterizedTest
    @MethodSource("params")
    fun testContentTags(input: String, expected: String) {
        resource("homepage.peb", input.trim())
        resource("templates/tags/hello.peb", "hello {{ tag.greeting }} {{ tag.content }} {{ tag.closing }}")
        resource("templates/tags/althello.peb", "alt {{ tag.greeting }} {{ tag.content }} {{ tag.closing }}")

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
                    "{% hello %}{% endhello %}",
                    "hello world"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' %}{% endhello %}",
                    "hello sir"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' 'and goodbye!' %}{% endhello %}",
                    "hello sir  and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' closing='and goodbye!' %}{% endhello %}",
                    "hello sir  and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello greeting='sir' %}{% endhello %}",
                    "hello sir"
                )
            )
            yield(
                Arguments.of(
                    "{% hello greeting='sir' closing=' and goodbye!' %}{% endhello %}",
                    "hello sir  and goodbye!"
                )
            )

            // simple tag, content with no filters
            yield(
                Arguments.of(
                    "{% hello %}from all of us{% endhello %}",
                    "hello world from all of us"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' %}from all of us{% endhello %}",
                    "hello sir from all of us"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' 'and goodbye!' %}from all of us{% endhello %}",
                    "hello sir from all of us and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' closing='and goodbye!' %}from all of us{% endhello %}",
                    "hello sir from all of us and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello greeting='sir' %}from all of us{% endhello %}",
                    "hello sir from all of us"
                )
            )
            yield(
                Arguments.of(
                    "{% hello greeting='sir' closing='and goodbye!' %}from all of us{% endhello %}",
                    "hello sir from all of us and goodbye!"
                )
            )

            // simple tag, content with filters
            yield(
                Arguments.of(
                    "{% hello :: upper %}from all of us{% endhello %}",
                    "hello world FROM ALL OF US"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' :: upper %}from all of us{% endhello %}",
                    "hello sir FROM ALL OF US"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' 'and goodbye!' :: upper %}from all of us{% endhello %}",
                    "hello sir FROM ALL OF US and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello 'sir' closing='and goodbye!' :: upper %}from all of us{% endhello %}",
                    "hello sir FROM ALL OF US and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello greeting='sir' :: upper %}from all of us{% endhello %}",
                    "hello sir FROM ALL OF US"
                )
            )
            yield(
                Arguments.of(
                    "{% hello greeting='sir' closing='and goodbye!' :: upper %}from all of us{% endhello %}",
                    "hello sir FROM ALL OF US and goodbye!"
                )
            )
            yield(
                Arguments.of(
                    "{% hello greeting='sir' closing='and goodbye!' template='althello' :: upper %}from all of us{% endhello %}",
                    "alt sir FROM ALL OF US and goodbye!"
                )
            )
        }.asIterable()
    }
}

class ContentHelloTag : TemplateTag("hello", Type.Content, true) {

    @Option
    @StringDefault("world")
    lateinit var greeting: String

    @Option
    lateinit var closing: String

    override fun parameters() = arrayOf(::greeting.name, ::closing.name)
}

@IgnoreModule
private class ContentTagModule : OrchidModule() {
    override fun configure() {
        addToSet<TemplateTag, ContentHelloTag>()
    }
}
