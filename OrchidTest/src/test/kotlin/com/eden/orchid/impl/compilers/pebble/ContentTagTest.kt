package com.eden.orchid.impl.compilers.pebble

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.utilities.addToSet
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat

class ContentTagTest : OrchidIntegrationTest(ContentTagModule(), withGenerator<HomepageGenerator>()) {

    @ParameterizedTest
    @CsvSource(
        "{% hello %}{% endhello %}, hello world",
        "{% hello 'sir' %}{% endhello %}, hello sir",
        "{% hello greeting='sir' %}{% endhello %}, hello sir",

        "{% hello %}from all of us{% endhello %}, hello world from all of us",
        "{% hello 'sir' %}from all of us{% endhello %}, hello sir from all of us",
        "{% hello greeting='sir' %}from all of us{% endhello %}, hello sir from all of us"
    )
    fun testSimpleTags(input: String, expected: String) {
        resource("homepage.peb", input.trim())
        resource("templates/tags/hello.peb", "hello {{ tag.greeting }} {{ tag.content }}")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            +expected.trim()
                        }
                    }
            }
    }
}

class ContentHelloTag : TemplateTag("hello", Type.Content, true) {

    @Option
    @StringDefault("world")
    lateinit var greeting: String

    override fun parameters() = arrayOf("greeting")

}

@IgnoreModule
private class ContentTagModule : OrchidModule() {
    override fun configure() {
        addToSet<TemplateTag, ContentHelloTag>()
    }
}