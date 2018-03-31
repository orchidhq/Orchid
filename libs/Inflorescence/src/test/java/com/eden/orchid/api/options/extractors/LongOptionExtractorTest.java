package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.LongDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class LongOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option @LongDefault(10L) public long testValue; }
    public static class TestClass2 { @Option @LongDefault(10L) public Long testValue; }
    public static class TestClass3 { @Option                   public long testValue; }

    public static class TestListClass1 {
        @Option @LongDefault({1L, 2L})
        public List<Long> testValues;
    }
    public static class TestListClass2 {
        @Option
        public List<Long> testValues;
    }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        LongConverter longConverter = new LongConverter(stringConverter);

        setupTest(new LongOptionExtractor(longConverter), longConverter, stringConverter);
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

    @ParameterizedTest
    @MethodSource("getOptionsListArguments")
    public void testExtractOptionList(
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

    static Stream<Arguments> getOptionsListArguments() {
        return Stream.of(
                Arguments.of(new TestListClass1(), "testValues", 1,                           new Long[] {1L}),
                Arguments.of(new TestListClass1(), "testValues", new Object[] {1, 0, 2, "3"}, new Long[] {1L, 0L, 2L, 3L}),
                Arguments.of(new TestListClass1(), "testValues", null,                        new Long[] {1L, 2L}),
                Arguments.of(new TestListClass1(), "testValues", "_nullValue",                new Long[] {1L, 2L}),
                Arguments.of(new TestListClass1(), "testValues", new String[0],               new Long[] {}),

                Arguments.of(new TestListClass2(), "testValues", 1,                           new Long[] {1L}),
                Arguments.of(new TestListClass2(), "testValues", new Object[] {1, 0, 2, "3"}, new Long[] {1L, 0L, 2L, 3L}),
                Arguments.of(new TestListClass2(), "testValues", null,                        new Long[] {}),
                Arguments.of(new TestListClass2(), "testValues", "_nullValue",                new Long[] {}),
                Arguments.of(new TestListClass2(), "testValues", new String[0],               new Long[] {})
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
                Arguments.of(new TestClass1(),     "testValue",  "10"),
                Arguments.of(new TestClass2(),     "testValue",  "10"),
                Arguments.of(new TestClass3(),     "testValue",  "0"),
                Arguments.of(new TestListClass1(), "testValues", "[1, 2]"),
                Arguments.of(new TestListClass2(), "testValues", "empty list")
        );
    }

}
