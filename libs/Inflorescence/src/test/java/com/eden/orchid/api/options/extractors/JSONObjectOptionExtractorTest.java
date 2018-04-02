package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import com.eden.orchid.api.options.converters.FlexibleMapConverter;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class JSONObjectOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass { @Option public JSONObject testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        FlexibleMapConverter mapConverter = new FlexibleMapConverter();

        setupTest(new JSONObjectOptionExtractor(mapConverter), stringConverter);
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("getOptionsArguments")
    public void testExtractOption(
            final Object underTest,
            final Object sourceValue,
            final Object expectedOriginalValue,
            final JSONObject expectedExtractedValue) throws Throwable {

        String optionName = "testValue";

        final JSONObject options = new JSONObject();
        if(sourceValue != null) {
            if(sourceValue.toString().equals("_nullValue")) {
                options.put(optionName, (String) null);
            }
            else {
                options.put(optionName, sourceValue);
            }
        }

        assertThat(underTest.getClass().getField(optionName).get(underTest), is(equalTo(expectedOriginalValue)));
        extractor.extractOptions(underTest, options);
        assertThat(expectedExtractedValue.similar(underTest.getClass().getField(optionName).get(underTest)), is(equalTo(true)));
    }

    static Stream<Arguments> getOptionsArguments() {
        return Stream.of(
                Arguments.of(new TestClass(), new JSONObject("{}"),                           null, new JSONObject("{}")),
                Arguments.of(new TestClass(), new JSONObject("{\"a\": 1, \"b\": 2}"),         null, new JSONObject("{\"a\": 1, \"b\": 2}")),
                Arguments.of(new TestClass(), new HashMap<>(),                                null, new JSONObject("{}")),
                Arguments.of(new TestClass(), new JSONObject("{\"a\": 1, \"b\": 2}").toMap(), null, new JSONObject("{\"a\": 1, \"b\": 2}")),
                Arguments.of(new TestClass(), null,                                           null, new JSONObject()),
                Arguments.of(new TestClass(), "_nullValue",                                   null, new JSONObject())
        );
    }

    @ParameterizedTest
    @MethodSource("getOptionsDescriptionArguments")
    public void testOptionsDescription(
            final Object underTest,
            final String expectedDescription) throws Throwable {
        super.testOptionDescription(
                underTest,
                expectedDescription
        );
    }

    static Stream<Arguments> getOptionsDescriptionArguments() {
        return Stream.of(
                Arguments.of(new TestClass(), "{}")
        );
    }

}
