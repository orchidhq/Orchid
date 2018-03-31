package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import com.eden.orchid.api.options.converters.FlexibleIterableConverter;
import com.eden.orchid.api.options.converters.FlexibleMapConverter;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.stream.Stream;

public class JSONArrayOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass { @Option public JSONArray testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        FlexibleMapConverter mapConverter = new FlexibleMapConverter();
        FlexibleIterableConverter iterableConverter = new FlexibleIterableConverter(() -> mapConverter);

        setupTest(new JSONArrayOptionExtractor(iterableConverter), stringConverter);
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("getOptionsArguments")
    public void testExtractOption(
            final Object underTest,
            final String optionName,
            final Object sourceValue,
            final Object[] expectedExtractedValue) throws Throwable {
        super.testExtractOptionList(
                underTest,
                optionName,
                sourceValue,
                expectedExtractedValue
        );
    }

    static Stream<Arguments> getOptionsArguments() {
        return Stream.of(
                Arguments.of(new TestClass(), "testValue", new JSONArray("[]"),                    new Object[] {}),
                Arguments.of(new TestClass(), "testValue", new JSONArray("[1, 2, 3]"),             new Object[] {1, 2, 3}),
                Arguments.of(new TestClass(), "testValue", new JSONArray("[\"1\", \"2\", \"3\"]"), new Object[] {"1", "2", "3"}),
                Arguments.of(new TestClass(), "testValue", null,                                   new Object[] {}),
                Arguments.of(new TestClass(), "testValue", "_nullValue",                           new Object[] {})
        );
    }

    @ParameterizedTest
    @MethodSource("getOptionsDescriptionArguments")
    public void testOptionsDescription(
            final Object underTest,
            final String optionName,
            final String expectedDescription) throws Throwable {
        super.testOptionDescription(
                underTest,
                optionName,
                expectedDescription
        );
    }

    static Stream<Arguments> getOptionsDescriptionArguments() {
        return Stream.of(
                Arguments.of(new TestClass(), "testValue", "[]")
        );
    }

}
