package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.LongDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class LongOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option @LongDefault(10L) public long testValue; }
    public static class TestClass2 { @Option @LongDefault(10L) public Long testValue; }
    public static class TestClass3 { @Option                   public long testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        LongConverter longConverter = new LongConverter(stringConverter);

        Set<OptionExtractor> extractors = new HashSet<>();
        extractors.add(new LongOptionExtractor(longConverter));

        setupTest(extractors);
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @Override
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
                Arguments.of(new TestClass1(), "testValue", 45,           0L, 45L),
                Arguments.of(new TestClass1(), "testValue", "45",         0L, 45L),
                Arguments.of(new TestClass1(), "testValue", null,         0L, 10L),
                Arguments.of(new TestClass1(), "testValue", "_nullValue", 0L, 10L),

                Arguments.of(new TestClass2(), "testValue", 45,           null, 45L),
                Arguments.of(new TestClass2(), "testValue", "45",         null, 45L),
                Arguments.of(new TestClass2(), "testValue", null,         null, 10L),
                Arguments.of(new TestClass2(), "testValue", "_nullValue", null, 10L),

                Arguments.of(new TestClass3(), "testValue", 45,           0L, 45L),
                Arguments.of(new TestClass3(), "testValue", "45",         0L, 45L),
                Arguments.of(new TestClass3(), "testValue", null,         0L, 0L),
                Arguments.of(new TestClass3(), "testValue", "_nullValue", 0L, 0L)
        );
    }

}
