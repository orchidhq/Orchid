package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.FloatConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.Extractor;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.FloatDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class FloatOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option @FloatDefault(10.0f) public float testValue; }
    public static class TestClass2 { @Option @FloatDefault(10.0f) public Float testValue; }
    public static class TestClass3 { @Option                      public float testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    private Extractor extractor;

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        FloatConverter floatConverter = new FloatConverter(stringConverter);

        Set<OptionExtractor> extractors = new HashSet<>();
        extractors.add(new FloatOptionExtractor(floatConverter));

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
                Arguments.of(new TestClass1(), "testValue", 45,           0.0f, 45.0f),
                Arguments.of(new TestClass1(), "testValue", 45.1,         0.0f, 45.1f),
                Arguments.of(new TestClass1(), "testValue", "45.1",       0.0f, 45.1f),
                Arguments.of(new TestClass1(), "testValue", null,         0.0f, 10.0f),
                Arguments.of(new TestClass1(), "testValue", "_nullValue", 0.0f, 10.0f),

                Arguments.of(new TestClass2(), "testValue", 45,           null, 45.0f),
                Arguments.of(new TestClass2(), "testValue", 45.1,         null, 45.1f),
                Arguments.of(new TestClass2(), "testValue", "45.1",       null, 45.1f),
                Arguments.of(new TestClass2(), "testValue", null,         null, 10.0f),
                Arguments.of(new TestClass2(), "testValue", "_nullValue", null, 10.0f),

                Arguments.of(new TestClass3(), "testValue", 45,           0.0f, 45.0f),
                Arguments.of(new TestClass3(), "testValue", 45.1,         0.0f, 45.1f),
                Arguments.of(new TestClass3(), "testValue", "45.1",       0.0f, 45.1f),
                Arguments.of(new TestClass3(), "testValue", null,         0.0f, 0.0f),
                Arguments.of(new TestClass3(), "testValue", "_nullValue", 0.0f, 0.0f)
        );
    }

}
