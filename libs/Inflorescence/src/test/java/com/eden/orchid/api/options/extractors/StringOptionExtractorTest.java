package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class StringOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option @StringDefault("defaultValue") public String testValue; }
    public static class TestClass2 { @Option                                public String testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());

        Set<OptionExtractor> extractors = new HashSet<>();
        extractors.add(new StringOptionExtractor(stringConverter));

        setupTest(extractors);
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("getOptionsArguments")
    public void testExtractOption(
            final Object underTest,
            final String optionName,
            final Object sourceValue,
            final Object expectedOriginalValue,
            final Object expectedExtractedValue) throws Throwable {
        super.testExtractOption(
                underTest,
                optionName,
                sourceValue,
                expectedOriginalValue,
                expectedExtractedValue
        );
    }

    static Stream<Arguments> getOptionsArguments() {
        return Stream.of(
                Arguments.of(new TestClass1(), "testValue", 45,               null, "45"),
                Arguments.of(new TestClass1(), "testValue", true,             null, "true"),
                Arguments.of(new TestClass1(), "testValue", false,            null, "false"),
                Arguments.of(new TestClass1(), "testValue", "45",             null, "45"),
                Arguments.of(new TestClass1(), "testValue", new JSONObject(), null, "{}"),
                Arguments.of(new TestClass1(), "testValue", null,             null, "defaultValue"),
                Arguments.of(new TestClass1(), "testValue", "_nullValue",     null, "defaultValue"),

                Arguments.of(new TestClass2(), "testValue", 45,               null, "45"),
                Arguments.of(new TestClass2(), "testValue", true,             null, "true"),
                Arguments.of(new TestClass2(), "testValue", false,            null, "false"),
                Arguments.of(new TestClass2(), "testValue", "45",             null, "45"),
                Arguments.of(new TestClass1(), "testValue", new JSONObject(), null, "{}"),
                Arguments.of(new TestClass2(), "testValue", null,             null, ""),
                Arguments.of(new TestClass2(), "testValue", "_nullValue",     null, "")
        );
    }

}
