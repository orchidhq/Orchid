package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.IntegerConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class IntOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option @IntDefault(10) public int     testValue; }
    public static class TestClass2 { @Option @IntDefault(10) public Integer testValue; }
    public static class TestClass3 { @Option                 public int     testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        IntegerConverter integerConverter = new IntegerConverter(stringConverter);

        Set<OptionExtractor> extractors = new HashSet<>();
        extractors.add(new IntOptionExtractor(integerConverter));

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
                Arguments.of(new TestClass1(), "testValue", 45,           0, 45),
                Arguments.of(new TestClass1(), "testValue", "45",         0, 45),
                Arguments.of(new TestClass1(), "testValue", null,         0, 10),
                Arguments.of(new TestClass1(), "testValue", "_nullValue", 0, 10),

                Arguments.of(new TestClass2(), "testValue", 45,           null, 45),
                Arguments.of(new TestClass2(), "testValue", "45",         null, 45),
                Arguments.of(new TestClass2(), "testValue", null,         null, 10),
                Arguments.of(new TestClass2(), "testValue", "_nullValue", null, 10),

                Arguments.of(new TestClass3(), "testValue", 45,           0, 45),
                Arguments.of(new TestClass3(), "testValue", "45",         0, 45),
                Arguments.of(new TestClass3(), "testValue", null,         0, 0),
                Arguments.of(new TestClass3(), "testValue", "_nullValue", 0, 0)
        );
    }

}
