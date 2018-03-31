package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class StringOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option @StringDefault("defaultValue") public String testValue; }
    public static class TestClass2 { @Option                                public String testValue; }

    public static class TestListClass1 {
        @Option @StringDefault({"defaultValue1", "defaultValue2"})
        public List<String> testValues;
    }
    public static class TestListClass2 {
        @Option
        public List<String> testValues;
    }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());

        setupTest(new StringOptionExtractor(stringConverter), stringConverter);
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
                Arguments.of(new TestClass2(), "testValue", new JSONObject(), null, "{}"),
                Arguments.of(new TestClass2(), "testValue", null,             null, ""),
                Arguments.of(new TestClass2(), "testValue", "_nullValue",     null, "")
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
                Arguments.of(new TestListClass1(), "testValues", 45,                              new String[] {"45"}),
                Arguments.of(new TestListClass1(), "testValues", new int[] {44, 45, 46},          new String[] {"44", "45", "46"}),
                Arguments.of(new TestListClass1(), "testValues", "45",                            new String[] {"45"}),
                Arguments.of(new TestListClass1(), "testValues", new String[] {"44", "45", "46"}, new String[] {"44", "45", "46"}),
                Arguments.of(new TestListClass1(), "testValues", null,                            new String[] {"defaultValue1", "defaultValue2"}),
                Arguments.of(new TestListClass1(), "testValues", "_nullValue",                    new String[] {"defaultValue1", "defaultValue2"}),
                Arguments.of(new TestListClass1(), "testValues", new String[0],                   new String[] {}),

                Arguments.of(new TestListClass2(), "testValues", 45,                              new String[] {"45"}),
                Arguments.of(new TestListClass2(), "testValues", new int[] {44, 45, 46},          new String[] {"44", "45", "46"}),
                Arguments.of(new TestListClass2(), "testValues", "45",                            new String[] {"45"}),
                Arguments.of(new TestListClass2(), "testValues", new String[] {"44", "45", "46"}, new String[] {"44", "45", "46"}),
                Arguments.of(new TestListClass2(), "testValues", null,                            new String[] {}),
                Arguments.of(new TestListClass2(), "testValues", "_nullValue",                    new String[] {}),
                Arguments.of(new TestListClass2(), "testValues", new String[0],                   new String[] {})
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
                Arguments.of(new TestClass1(),     "testValue",  "defaultValue"),
                Arguments.of(new TestClass2(),     "testValue",  "empty string"),
                Arguments.of(new TestListClass1(), "testValues", "[defaultValue1, defaultValue2]"),
                Arguments.of(new TestListClass2(), "testValues", "empty list")
        );
    }

}
