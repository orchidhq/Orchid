package com.eden.orchid.languages.asciidoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.languages.asciidoc.extensions.AsciidocIncludeProcessor
import com.eden.orchid.testhelpers.OrchidUnitTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.get
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import javax.inject.Provider

class AsciidocIncludeUnitTests : OrchidUnitTest {

    private lateinit var context: OrchidContext
    private lateinit var underTest: AsciidocIncludeProcessor

    @BeforeEach
    fun setUp() {
        context = mock(OrchidContext::class.java)
        underTest = AsciidocIncludeProcessor(Provider { context })
    }

    @Test
    @DisplayName("Test that tagged sections can be found with basic regex, and can be nested properly")
    fun test01() {
        val input = """
            |// tag::snippets[]
            |// tag::snippet-a[]
            |snippet a
            |// end::snippet-a[]
            |
            |// tag::snippet-b[]
            |snippet b
            |// end::snippet-b[]
            |// end::snippets[]
        """.trimMargin()

        val result = underTest.getTaggedSections(input).entries.sortedBy { it.key.minOrNull()!! }

        expectThat(result)
            .hasSize(3)
            .and {
                this[0]
                    .and { get { key }.isEqualTo("snippet-a") }
                    .and { get { value }.containsExactly("snippet a") }
            }
            .and {
                this[1]
                    .and { get { key }.isEqualTo("snippet-b") }
                    .and { get { value }.containsExactly("snippet b") }
            }
            .and {
                this[2]
                    .and { get { key }.isEqualTo("snippets") }
                    .and { get { value }.containsExactly("") }
            }

        expectThat(underTest.getIncludedTags(input, "snippet-a", false))
            .isEqualTo(
                """
                |snippet a
                """.trimMargin()
            )
        expectThat(underTest.getIncludedTags(input, "snippet-b", false))
            .isEqualTo(
                """
                |snippet b
                """.trimMargin()
            )
        expectThat(underTest.getIncludedTags(input, "snippet-a;snippet-b", false))
            .isEqualTo(
                """
                |snippet a
                |snippet b
                """.trimMargin()
            )
        expectThat(underTest.getIncludedTags(input, "snippets", false))
            .isEqualTo(
                """
                |
                """.trimMargin()
            )
        expectThat(underTest.getIncludedTags(input, "*", false))
            .isEqualTo(
                """
                |snippet a
                |snippet b
                |
                """.trimMargin()
            )
    }

    @Test
    @DisplayName(
        "Test that tagged sections can be found with basic regex. Parent sections that are not closed " +
            "properly are not included"
    )
    fun test02() {
        val input = """
            |// tag::snippets[]
            |// tag::snippet-a[]
            |snippet a
            |// end::snippet-a[]
            |
            |// tag::snippet-b[]
            |snippet b
            |// end::snippet-b[]
        """.trimMargin()

        val result = underTest.getTaggedSections(input).entries.sortedBy { it.key.minOrNull()!! }

        expectThat(result)
            .hasSize(2)
            .and {
                this[0]
                    .and { get { key }.isEqualTo("snippet-a") }
                    .and { get { value }.containsExactly("snippet a") }
            }
            .and {
                this[1]
                    .and { get { key }.isEqualTo("snippet-b") }
                    .and { get { value }.containsExactly("snippet b") }
            }
    }

    @Test
    @DisplayName("Test that tagged sections are filtered properly by input attribute")
    fun test03() {
        val taggedSections = mapOf(
            "one" to listOf("a", "b", "c"),
            "two" to listOf("d", "e", "f"),
            "three" to listOf("g", "h", "i")
        )

        expectThat(underTest.getActualTags(taggedSections, "*", false))
            .containsExactlyInAnyOrder("one", "two", "three")
        expectThat(underTest.getActualTags(taggedSections, "one", false))
            .containsExactlyInAnyOrder("one")
        expectThat(underTest.getActualTags(taggedSections, "one;two", false))
            .containsExactlyInAnyOrder("one", "two")
        expectThat(underTest.getActualTags(taggedSections, "one,three", false))
            .containsExactlyInAnyOrder("one", "three")
        expectThat(underTest.getActualTags(taggedSections, "one,four", false))
            .containsExactlyInAnyOrder("one")

        expectThat(underTest.getActualTags(taggedSections, "*", true))
            .containsExactlyInAnyOrder()
        expectThat(underTest.getActualTags(taggedSections, "one", true))
            .containsExactlyInAnyOrder("one")
        expectThat(underTest.getActualTags(taggedSections, "one;two", true))
            .containsExactlyInAnyOrder()
        expectThat(underTest.getActualTags(taggedSections, "one,three", true))
            .containsExactlyInAnyOrder()
        expectThat(underTest.getActualTags(taggedSections, "one,four", true))
            .containsExactlyInAnyOrder()

        expectThat(underTest.getTagContent(taggedSections, listOf("one")))
            .isEqualTo(
                """
                |a
                |b
                |c
                """.trimMargin()
            )
        expectThat(underTest.getTagContent(taggedSections, listOf("two")))
            .isEqualTo(
                """
                |d
                |e
                |f
                """.trimMargin()
            )
        expectThat(underTest.getTagContent(taggedSections, listOf("three")))
            .isEqualTo(
                """
                |g
                |h
                |i
                """.trimMargin()
            )
        expectThat(underTest.getTagContent(taggedSections, listOf("one", "two")))
            .isEqualTo(
                """
                |a
                |b
                |c
                |d
                |e
                |f
                """.trimMargin()
            )
        expectThat(underTest.getTagContent(taggedSections, listOf("two", "three")))
            .isEqualTo(
                """
                |d
                |e
                |f
                |g
                |h
                |i
                """.trimMargin()
            )
        expectThat(underTest.getTagContent(taggedSections, listOf("one", "three")))
            .isEqualTo(
                """
                |a
                |b
                |c
                |g
                |h
                |i
                """.trimMargin()
            )
        expectThat(underTest.getTagContent(taggedSections, listOf("one", "two", "three")))
            .isEqualTo(
                """
                |a
                |b
                |c
                |d
                |e
                |f
                |g
                |h
                |i
                """.trimMargin()
            )
    }

    @Test
    @DisplayName("Test that lines are filtered properly by input attribute")
    fun test04() {
        val input = """
            |tag::includedBlock1[]
            |Content in block 1
            |end::includedBlock1[]
            |
            |tag::includedBlock2[]
            |Content in block 2
            |end::includedBlock2[]
        """.trimMargin()

        val inputLines = input.lines()

        expectThat(underTest.getActualLines(inputLines, "1..-1")).containsExactly(1, 2, 3, 4, 5, 6, 7)
        expectThat(underTest.getActualLines(inputLines, "1..-2")).containsExactly(1, 2, 3, 4, 5, 6)
        expectThat(underTest.getActualLines(inputLines, "5..7")).containsExactly(5, 6, 7)
        expectThat(underTest.getActualLines(inputLines, "5;6..7")).containsExactly(5, 6, 7)
        expectThat(underTest.getActualLines(inputLines, "5,6..7")).containsExactly(5, 6, 7)
        expectThat(underTest.getActualLines(inputLines, "5")).containsExactly(5)
        expectThat(underTest.getActualLines(inputLines, "-2")).containsExactly(6)
        expectThat(underTest.getActualLines(inputLines, "6;5")).containsExactly(5, 6)

        expectThat(underTest.getIncludedLines(input, "1..-1"))
            .isEqualTo(
                """
                |tag::includedBlock1[]
                |Content in block 1
                |end::includedBlock1[]
                |
                |tag::includedBlock2[]
                |Content in block 2
                |end::includedBlock2[]
                """.trimMargin()
            )
        expectThat(underTest.getIncludedLines(input, "1..-2"))
            .isEqualTo(
                """
                |tag::includedBlock1[]
                |Content in block 1
                |end::includedBlock1[]
                |
                |tag::includedBlock2[]
                |Content in block 2
                """.trimMargin()
            )
        expectThat(underTest.getIncludedLines(input, "5..7"))
            .isEqualTo(
                """
                |tag::includedBlock2[]
                |Content in block 2
                |end::includedBlock2[]
                """.trimMargin()
            )
        expectThat(underTest.getIncludedLines(input, "5;6..7"))
            .isEqualTo(
                """
                |tag::includedBlock2[]
                |Content in block 2
                |end::includedBlock2[]
                """.trimMargin()
            )
        expectThat(underTest.getIncludedLines(input, "5,6..7"))
            .isEqualTo(
                """
                |tag::includedBlock2[]
                |Content in block 2
                |end::includedBlock2[]
                """.trimMargin()
            )
        expectThat(underTest.getIncludedLines(input, "5"))
            .isEqualTo(
                """
                |tag::includedBlock2[]
                """.trimMargin()
            )
        expectThat(underTest.getIncludedLines(input, "-2"))
            .isEqualTo(
                """
                |Content in block 2
                """.trimMargin()
            )
        expectThat(underTest.getIncludedLines(input, "6;5"))
            .isEqualTo(
                """
                |tag::includedBlock2[]
                |Content in block 2
                """.trimMargin()
            )
    }
}
