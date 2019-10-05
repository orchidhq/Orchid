package com.eden.orchid.impl.compilers.parsers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.assertions.succeeded

class JsonParserTest {

    lateinit var underTest: JsonParser

    @BeforeEach
    internal fun setUp() {
        underTest = JsonParser()
    }

    @Test
    fun testJsonObjectSyntaxError() {
        val input = """
            |{
            |   "asdf": "asdf
            |}
            """.trimMargin()

        expectCatching {
            underTest.parse("json", input)
        }.succeeded()
    }

    @Test
    fun testJsonArraySyntaxError() {
        val input = """
            |[
            |   "asdf
            |]
            """.trimMargin()

        expectCatching {
            underTest.parse("json", input)
        }.succeeded()
    }
}
