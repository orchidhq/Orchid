package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.BooleanConverter;
import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.converters.NumberConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class BooleanOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option @BooleanDefault(true) public boolean testValue; }
    public static class TestClass2 { @Option @BooleanDefault(true) public Boolean testValue; }
    public static class TestClass3 { @Option                       public boolean testValue; }

    public static class TestListClass1 {
        @Option @BooleanDefault({true, false})
        public List<Boolean> testValue;
    }
    public static class TestListClass2 {
        @Option
        public List<Boolean> testValue;
    }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        LongConverter longConverter = new LongConverter(stringConverter);
        DoubleConverter doubleConverter = new DoubleConverter(stringConverter);
        NumberConverter numberConverter = new NumberConverter(longConverter, doubleConverter);
        BooleanConverter booleanConverter = new BooleanConverter(stringConverter, numberConverter);

        setupTest(new BooleanOptionExtractor(booleanConverter), booleanConverter, stringConverter);
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
                Arguments.of(new TestClass1(), 0,                false, false),
                Arguments.of(new TestClass1(), 1,                false, true),
                Arguments.of(new TestClass1(), 1.1,              false, true),
                Arguments.of(new TestClass1(), true,             false, true),
                Arguments.of(new TestClass1(), false,            false, false),
                Arguments.of(new TestClass1(), "true",           false, true),
                Arguments.of(new TestClass1(), "false",          false, false),
                Arguments.of(new TestClass1(), new JSONObject(), false, false),
                Arguments.of(new TestClass1(), "null",           false, false),
                Arguments.of(new TestClass1(), null,             false, true),
                Arguments.of(new TestClass1(), "_nullValue",     false, true),

                Arguments.of(new TestClass2(), 0,                null, false),
                Arguments.of(new TestClass2(), 1,                null, true),
                Arguments.of(new TestClass2(), 1.1,              null, true),
                Arguments.of(new TestClass2(), true,             null, true),
                Arguments.of(new TestClass2(), false,            null, false),
                Arguments.of(new TestClass2(), "true",           null, true),
                Arguments.of(new TestClass2(), "false",          null, false),
                Arguments.of(new TestClass2(), new JSONObject(), null, false),
                Arguments.of(new TestClass2(), "null",           null, false),
                Arguments.of(new TestClass2(), null,             null, true),
                Arguments.of(new TestClass2(), "_nullValue",     null, true),

                Arguments.of(new TestClass3(), 0,                false, false),
                Arguments.of(new TestClass3(), 1,                false, true),
                Arguments.of(new TestClass3(), 1.1,              false, true),
                Arguments.of(new TestClass3(), true,             false, true),
                Arguments.of(new TestClass3(), false,            false, false),
                Arguments.of(new TestClass3(), "true",           false, true),
                Arguments.of(new TestClass3(), "false",          false, false),
                Arguments.of(new TestClass3(), new JSONObject(), false, false),
                Arguments.of(new TestClass3(), "null",           false, false),
                Arguments.of(new TestClass3(), null,             false, false),
                Arguments.of(new TestClass3(), "_nullValue",     false, false)
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
                Arguments.of(new TestListClass1(), true,                              new Boolean[] {true}),
                Arguments.of(new TestListClass1(), new Object[] {1, 0, 1.1, "false"}, new Boolean[] {true, false, true, false}),
                Arguments.of(new TestListClass1(), null,                              new Boolean[] {true, false}),
                Arguments.of(new TestListClass1(), "_nullValue",                      new Boolean[] {true, false}),
                Arguments.of(new TestListClass1(), new String[0],                     new Boolean[] {}),

                Arguments.of(new TestListClass2(), true,                              new Boolean[] {true}),
                Arguments.of(new TestListClass2(), new Object[] {1, 0, 1.1, "false"}, new Boolean[] {true, false, true, false}),
                Arguments.of(new TestListClass2(), null,                              new Boolean[] {}),
                Arguments.of(new TestListClass2(), "_nullValue",                      new Boolean[] {}),
                Arguments.of(new TestListClass2(), new String[0],                     new Boolean[] {})
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
                Arguments.of(new TestClass1(),     "true"),
                Arguments.of(new TestClass2(),     "true"),
                Arguments.of(new TestClass3(),     "false"),
                Arguments.of(new TestListClass1(), "[true, false]"),
                Arguments.of(new TestListClass2(), "empty list")
        );
    }

}
