package com.eden.orchid.testhelpers.com.eden.orchid.utilities

import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.dashCase
import com.eden.orchid.utilities.filename
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.pascalCase
import com.eden.orchid.utilities.slug
import com.eden.orchid.utilities.snakeCase
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import com.eden.orchid.utilities.words
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class StringConversion {

    @TestFactory
    fun testFromCamelCase(): List<DynamicTest> {
        val splitter = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { camelCase(m) } to { titleCase() }
            else
                s.from { camelCase() } to { titleCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("oneTwoThree") { expectThat(splitter("oneTwoThree", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("oneTwoThree (mapped)") { expectThat(splitter("oneTwoThree") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") }
        )
    }

    @TestFactory
    fun testFromSnakeCase(): List<DynamicTest> {
        val splitter = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { snakeCase(m) } to { titleCase() }
            else
                s.from { snakeCase() } to { titleCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("one_two_three") { expectThat(splitter("one_two_three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one_two_three (mapped)") { expectThat(splitter("one_two_three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") }
        )
    }

    @TestFactory
    fun testFromDashCase(): List<DynamicTest> {
        val splitter = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { dashCase(m) } to { titleCase() }
            else
                s.from { dashCase() } to { titleCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("one-two-three") { expectThat(splitter("one-two-three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one-two-three (mapped)") { expectThat(splitter("one-two-three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") }
        )
    }

    @TestFactory
    fun testFromWords(): List<DynamicTest> {
        val splitter = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { words(m) } to { titleCase() }
            else
                s.from { words() } to { titleCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("one two three") { expectThat(splitter("one two three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one two three (mapped)") { expectThat(splitter("one two three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") }
        )
    }

    @TestFactory
    fun testFromFilename(): List<DynamicTest> {
        val splitter = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { filename(m) } to { titleCase() }
            else
                s.from { filename() } to { titleCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("one two three") { expectThat(splitter("one two three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one-two-three") { expectThat(splitter("one-two-three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one_two_three") { expectThat(splitter("one_two_three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("oneTwoThree") { expectThat(splitter("oneTwoThree", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one two-three") { expectThat(splitter("one two-three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one two_three") { expectThat(splitter("one two_three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one twoThree") { expectThat(splitter("one twoThree", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one-two three") { expectThat(splitter("one-two three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one-two_three") { expectThat(splitter("one-two_three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one-twoThree") { expectThat(splitter("one-twoThree", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one_two three") { expectThat(splitter("one_two three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one_two-three") { expectThat(splitter("one_two-three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("one_twoThree") { expectThat(splitter("one_twoThree", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("oneTwo three") { expectThat(splitter("oneTwo three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("oneTwo-three") { expectThat(splitter("oneTwo-three", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("oneTwo_three") { expectThat(splitter("oneTwo_three", null)).isEqualTo("One Two Three") },

                DynamicTest.dynamicTest("one two three (mapped)") { expectThat(splitter("one two three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one-two-three (mapped)") { expectThat(splitter("one-two-three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one_two_three (mapped)") { expectThat(splitter("one_two_three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("oneTwoThree (mapped)") { expectThat(splitter("oneTwoThree") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one two-three (mapped)") { expectThat(splitter("one two-three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one two_three (mapped)") { expectThat(splitter("one two_three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one twoThree (mapped)") { expectThat(splitter("one twoThree") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one-two three (mapped)") { expectThat(splitter("one-two three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one-two_three (mapped)") { expectThat(splitter("one-two_three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one-twoThree (mapped)") { expectThat(splitter("one-twoThree") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one_two three (mapped)") { expectThat(splitter("one_two three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one_two-three (mapped)") { expectThat(splitter("one_two-three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("one_twoThree (mapped)") { expectThat(splitter("one_twoThree") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("oneTwo three (mapped)") { expectThat(splitter("oneTwo three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("oneTwo-three (mapped)") { expectThat(splitter("oneTwo-three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") },
                DynamicTest.dynamicTest("oneTwo_three (mapped)") { expectThat(splitter("oneTwo_three") { it.toUpperCase() }).isEqualTo("ONE TWO THREE") }
        )
    }

    @TestFactory
    fun testToPascalCase(): List<DynamicTest> {
        val joiner = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { camelCase() } to { pascalCase(m) }
            else
                s.from { camelCase() } to { pascalCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("oneTwoThree") { expectThat(joiner("oneTwoThree", null)).isEqualTo("OneTwoThree") },
                DynamicTest.dynamicTest("oneTwoThree (mapped)") { expectThat(joiner("oneTwoThree") { it + it }).isEqualTo("OneoneTwotwoThreethree") }
        )
    }

    @TestFactory
    fun testToCamelCase(): List<DynamicTest> {
        val joiner = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { camelCase() } to { camelCase(m) }
            else
                s.from { camelCase() } to { camelCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("oneTwoThree") { expectThat(joiner("oneTwoThree", null)).isEqualTo("oneTwoThree") },
                DynamicTest.dynamicTest("oneTwoThree (mapped)") { expectThat(joiner("oneTwoThree") { it + it }).isEqualTo("oneoneTwotwoThreethree") }
        )
    }

    @TestFactory
    fun testToWords(): List<DynamicTest> {
        val joiner = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { camelCase() } to { words(m) }
            else
                s.from { camelCase() } to { words() }
        }

        return listOf(
                DynamicTest.dynamicTest("oneTwoThree") { expectThat(joiner("oneTwoThree", null)).isEqualTo("one Two Three") },
                DynamicTest.dynamicTest("oneTwoThree (mapped)") { expectThat(joiner("oneTwoThree") { it + it }).isEqualTo("oneone TwoTwo ThreeThree") }
        )
    }

    @TestFactory
    fun testToSnakeCase(): List<DynamicTest> {
        val joiner = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { camelCase() } to { snakeCase(m) }
            else
                s.from { camelCase() } to { snakeCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("oneTwoThree") { expectThat(joiner("oneTwoThree", null)).isEqualTo("ONE_TWO_THREE") },
                DynamicTest.dynamicTest("oneTwoThree (mapped)") { expectThat(joiner("oneTwoThree") { it + it }).isEqualTo("ONEONE_TWOTWO_THREETHREE") }
        )
    }

    @TestFactory
    fun testToDashCase(): List<DynamicTest> {
        val joiner = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { camelCase() } to { dashCase(m) }
            else
                s.from { camelCase() } to { dashCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("oneTwoThree") { expectThat(joiner("oneTwoThree", null)).isEqualTo("one-Two-Three") },
                DynamicTest.dynamicTest("oneTwoThree (mapped)") { expectThat(joiner("oneTwoThree") { it + it }).isEqualTo("oneone-TwoTwo-ThreeThree") }
        )
    }

    @TestFactory
    fun testToSlug(): List<DynamicTest> {
        val joiner = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { camelCase() } to { slug(m) }
            else
                s.from { camelCase() } to { slug() }
        }

        return listOf(
                DynamicTest.dynamicTest("oneTwoThree") { expectThat(joiner("oneTwoThree", null)).isEqualTo("one-two-three") },
                DynamicTest.dynamicTest("oneTwoThree (mapped)") { expectThat(joiner("oneTwoThree") { it + it }).isEqualTo("oneone-twotwo-threethree") }
        )
    }

    @TestFactory
    fun testToTitleCase(): List<DynamicTest> {
        val joiner = { s: String, m: ((String) -> String)? ->
            if (m != null)
                s.from { camelCase() } to { titleCase(m) }
            else
                s.from { camelCase() } to { titleCase() }
        }

        return listOf(
                DynamicTest.dynamicTest("oneTwoThree") { expectThat(joiner("oneTwoThree", null)).isEqualTo("One Two Three") },
                DynamicTest.dynamicTest("oneTwoThree (mapped)") { expectThat(joiner("oneTwoThree") { it + it }).isEqualTo("Oneone TwoTwo ThreeThree") }
        )
    }

}