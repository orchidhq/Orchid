package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class AnyOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option public Object testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());

        setupTest(new AnyOptionExtractor(), stringConverter);
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("getOptionsArguments")
    public void testExtractOption(
            final Object underTest,
            final Object sourceValue,
            final Object expectedOriginalValue,
            final Object expectedExtractedValue) throws Throwable {
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
        assertThat(underTest.getClass().getField(optionName).get(underTest), is(equalTo(expectedExtractedValue)));
        if(sourceValue != null) {
            if(sourceValue.toString().equals("_nullValue")) {
                assertThat(underTest.getClass().getField(optionName).get(underTest), is(nullValue()));
            }
            else {
                assertThat(underTest.getClass().getField(optionName).get(underTest), is(sameInstance(sourceValue)));
            }
        }
    }

    static Stream<Arguments> getOptionsArguments() {
        return Stream.of(
                Arguments.of(new TestClass1(), 0,                null, 0),
                Arguments.of(new TestClass1(), 1,                null, 1),
                Arguments.of(new TestClass1(), 1.1,              null, 1.1),
                Arguments.of(new TestClass1(), true,             null, true),
                Arguments.of(new TestClass1(), false,            null, false),
                Arguments.of(new TestClass1(), "true",           null, "true"),
                Arguments.of(new TestClass1(), "false",          null, "false"),
                Arguments.of(new TestClass1(), "null",           null, "null"),
                Arguments.of(new TestClass1(), null,             null, null),
                Arguments.of(new TestClass1(), "_nullValue",     null, null)
        );
    }

}
