package com.eden.orchid.api.options.archetypes

import com.eden.common.json.JSONElement
import com.eden.common.util.EdenPair
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.converters.ClogStringConverterHelper
import com.eden.orchid.api.converters.Converters
import com.eden.orchid.api.converters.FlexibleIterableConverter
import com.eden.orchid.api.converters.FlexibleMapConverter
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.extractors.ListOptionExtractor
import com.eden.orchid.api.options.extractors.StringArrayOptionExtractor
import com.eden.orchid.api.options.extractors.StringOptionExtractor
import com.eden.orchid.testhelpers.OrchidUnitTest
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SharedConfigArchetypeTest : OrchidUnitTest() {

    private lateinit var context: OrchidContext
    private lateinit var extractors: Set<OptionExtractor<*>>
    private lateinit var extractor: OptionsExtractor
    private lateinit var underTest: TestClassWithSharedConfigArchetype

    @BeforeEach
    fun setUp() {
        context = mock(OrchidContext::class.java)

        val mapConverter = FlexibleMapConverter()
        val iterableConverter = FlexibleIterableConverter(mapConverter)
        val stringConverter = StringConverter(setOf(ClogStringConverterHelper()))
        val converters = Converters(setOf(mapConverter, iterableConverter, stringConverter))

        extractors = setOf(
            StringOptionExtractor(stringConverter),
            ListOptionExtractor({ extractor }, iterableConverter, mapConverter, converters),
            StringArrayOptionExtractor(iterableConverter, converters)
        )
        extractor = OptionsExtractor(context, extractors, null)

        `when`(context.getEmbeddedData(anyString(), anyString())).thenReturn(EdenPair("", emptyMap()))
        `when`(context.resolve(OptionsExtractor::class.java)).thenReturn(extractor)
        `when`(context.resolve(SharedConfigArchetype::class.java)).thenAnswer { SharedConfigArchetype(context) }

        `when`(context.query("config.one")).thenReturn(
            JSONElement(
                JSONObject(
                    mapOf(
                        "key" to "value 1",
                        "val1" to "value 1"
                    )
                )
            )
        )
        `when`(context.query("config.two")).thenReturn(
            JSONElement(
                JSONObject(
                    mapOf(
                        "key" to "value 2",
                        "val2" to "value 2"
                    )
                )
            )
        )
        `when`(context.query("config.three")).thenReturn(
            JSONElement(
                JSONObject(
                    mapOf(
                        "key" to "value 3",
                        "val3" to "value 3"
                    )
                )
            )
        )

        underTest = TestClassWithSharedConfigArchetype()
    }

    @Archetype(SharedConfigArchetype::class, key = "from")
    class TestClassWithSharedConfigArchetype : OptionsHolder {

        @Option
        lateinit var key: String

        @Option
        lateinit var val1: String

        @Option
        lateinit var val2: String

        @Option
        lateinit var val3: String

    }

    @ParameterizedTest
    @CsvSource(
        // from      , key    , val1   , val2    , val3
        "config.one  , value 1, value 1, ''      , ''     ",
        "config.two  , value 2, ''     , value 2 , ''     ",
        "config.three, value 3, ''     , ''      , value 3"
    )
    fun testSingleSharedConfig(from: String, key: String, val1: String, val2: String, val3: String) {
        underTest.extractOptions(context, mapOf("from" to from))
        expectThat(underTest.key).isEqualTo(key)
        expectThat(underTest.val1).isEqualTo(val1)
        expectThat(underTest.val2).isEqualTo(val2)
        expectThat(underTest.val3).isEqualTo(val3)
    }

    @ParameterizedTest
    @CsvSource(
        // from1     , from2       , key    , val1   , val2    , val3
        "config.one  , config.two  , value 2, value 1, value 2 , ''     ",
        "config.two  , config.one  , value 1, value 1, value 2 , ''     ",

        "config.two  , config.three, value 3, ''     , value 2 , value 3",
        "config.three, config.two  , value 2, ''     , value 2 , value 3",

        "config.three, config.one  , value 1, value 1, ''      , value 3",
        "config.one  , config.three, value 3, value 1, ''      , value 3"
    )
    fun testMultipleSharedConfig(from1: String, from2: String, key: String, val1: String, val2: String, val3: String) {
        underTest.extractOptions(context, mapOf("from" to listOf(from1, from2)))
        expectThat(underTest.key).isEqualTo(key)
        expectThat(underTest.val1).isEqualTo(val1)
        expectThat(underTest.val2).isEqualTo(val2)
        expectThat(underTest.val3).isEqualTo(val3)
    }

}