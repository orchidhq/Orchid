package com.eden.orchid.impl.compilers.parsers

import com.eden.orchid.testhelpers.OrchidUnitTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.assertions.isSuccess

class TOMLParserTest : OrchidUnitTest {

    lateinit var underTest: TOMLParser

    @BeforeEach
    internal fun setUp() {
        underTest = TOMLParser()
    }

    @Test
    fun testTomlError() {
        val input = """
            |+++
            |title = 'asdf
            |+++
            """.trimMargin()

        expectCatching {
            underTest.parse("toml", input)
        }.isSuccess()
    }
}
