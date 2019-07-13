package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo

class BaseUrlFunctionTest {

    lateinit var context: OrchidContext
    lateinit var underTest: BaseUrlFunction

    @BeforeEach
    fun setUp() {
        context = mock(OrchidContext::class.java)
        underTest = BaseUrlFunction()
    }

    @Test
    fun parameters() {
        expectThat(underTest.parameters().toList())
            .containsExactly("input")
    }

    @ParameterizedTest
    @CsvSource(
        // | Base URL            | Input     | Expected |
        "/                       , homepage  , /homepage",
        "/                       , /homepage , /homepage",
        "/                       , homepage/ , /homepage",
        "/                       , /homepage/, /homepage",

        "/base                   , homepage  , /base/homepage",
        "/base                   , /homepage , /base/homepage",
        "/base                   , homepage/ , /base/homepage",
        "/base                   , /homepage/, /base/homepage",

        "http://example.com      , homepage  , http://example.com/homepage",
        "http://example.com      , /homepage , http://example.com/homepage",
        "http://example.com      , homepage/ , http://example.com/homepage",
        "http://example.com      , /homepage/, http://example.com/homepage",

        "http://example.com/base , homepage  , http://example.com/base/homepage",
        "http://example.com/base , /homepage , http://example.com/base/homepage",
        "http://example.com/base , homepage/ , http://example.com/base/homepage",
        "http://example.com/base , /homepage/, http://example.com/base/homepage"
    )
    fun apply(baseUrl: String, input: String, expected: String) {
        `when`(context.baseUrl).thenReturn(baseUrl)
        underTest.input = input

        expectThat(underTest.apply(context, null)).isEqualTo(expected)
    }
}