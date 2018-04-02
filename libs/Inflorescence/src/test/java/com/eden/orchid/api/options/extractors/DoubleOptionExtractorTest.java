package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.DoubleDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class DoubleOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option @DoubleDefault(10.0) public double testValue; }
    public static class TestClass2 { @Option @DoubleDefault(10.0) public Double testValue; }
    public static class TestClass3 { @Option                      public double testValue; }

    public static class TestListClass1 {
        @Option @DoubleDefault({1.1, 2.2})
        public List<Double> testValue;
    }
    public static class TestListClass2 {
        @Option
        public List<Double> testValue;
    }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        DoubleConverter doubleConverter = new DoubleConverter(stringConverter);

        setupTest(new DoubleOptionExtractor(doubleConverter), doubleConverter, stringConverter);
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
        super.testExtractOption(
                underTest,
                sourceValue,
                expectedOriginalValue,
                expectedExtractedValue
        );
    }

    static Stream<Arguments> getOptionsArguments() {
        return Stream.of(
                Arguments.of(new TestClass1(), 45,           0.0, 45.0),
                Arguments.of(new TestClass1(), 45.1,         0.0, 45.1),
                Arguments.of(new TestClass1(), "45.1",       0.0, 45.1),
                Arguments.of(new TestClass1(), null,         0.0, 10.0),
                Arguments.of(new TestClass1(), "_nullValue", 0.0, 10.0),

                Arguments.of(new TestClass2(), 45,           null, 45.0),
                Arguments.of(new TestClass2(), 45.1,         null, 45.1),
                Arguments.of(new TestClass2(), "45.1",       null, 45.1),
                Arguments.of(new TestClass2(), null,         null, 10.0),
                Arguments.of(new TestClass2(), "_nullValue", null, 10.0),

                Arguments.of(new TestClass3(), 45,           0.0, 45.0),
                Arguments.of(new TestClass3(), 45.1,         0.0, 45.1),
                Arguments.of(new TestClass3(), "45.1",       0.0, 45.1),
                Arguments.of(new TestClass3(), null,         0.0, 0.0),
                Arguments.of(new TestClass3(), "_nullValue", 0.0, 0.0)
        );
    }

    @ParameterizedTest
    @MethodSource("getOptionsListArguments")
    public void testExtractOptionList(
            final Object underTest,
            final Object sourceValue,
            final Object[] expectedExtractedValue) throws Throwable {
        super.testExtractOptionList(
                underTest,
                sourceValue,
                expectedExtractedValue
        );
    }

    static Stream<Arguments> getOptionsListArguments() {
        return Stream.of(
                Arguments.of(new TestListClass1(), 1.1,                             new Double[] {1.1}),
                Arguments.of(new TestListClass1(), new Object[] {1, 0, 1.1, "3.3"}, new Double[] {1.0, 0.0, 1.1, 3.3}),
                Arguments.of(new TestListClass1(), null,                            new Double[] {1.1, 2.2}),
                Arguments.of(new TestListClass1(), "_nullValue",                    new Double[] {1.1, 2.2}),
                Arguments.of(new TestListClass1(), new String[0],                   new Double[] {}),

                Arguments.of(new TestListClass2(), 1.1,                             new Double[] {1.1}),
                Arguments.of(new TestListClass2(), new Object[] {1, 0, 1.1, "3.3"}, new Double[] {1.0, 0.0, 1.1, 3.3}),
                Arguments.of(new TestListClass2(), null,                            new Double[] {}),
                Arguments.of(new TestListClass2(), "_nullValue",                    new Double[] {}),
                Arguments.of(new TestListClass2(), new String[0],                   new Double[] {})
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
                Arguments.of(new TestClass1(),     "10.0"),
                Arguments.of(new TestClass2(),     "10.0"),
                Arguments.of(new TestClass3(),     "0.0"),
                Arguments.of(new TestListClass1(), "[1.1, 2.2]"),
                Arguments.of(new TestListClass2(), "empty list")
        );
    }

}
