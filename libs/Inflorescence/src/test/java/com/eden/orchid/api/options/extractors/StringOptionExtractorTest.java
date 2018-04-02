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
        public List<String> testValue;
    }
    public static class TestListClass2 {
        @Option
        public List<String> testValue;
    }

    public static class TestArrayClass1 {
        @Option @StringDefault({"defaultValue1", "defaultValue2"})
        public String[] testValue;
    }
    public static class TestArrayClass2 {
        @Option
        public String[] testValue;
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
                Arguments.of(new TestClass1(), 45,               null, "45"),
                Arguments.of(new TestClass1(), true,             null, "true"),
                Arguments.of(new TestClass1(), false,            null, "false"),
                Arguments.of(new TestClass1(), "45",             null, "45"),
                Arguments.of(new TestClass1(), new JSONObject(), null, "{}"),
                Arguments.of(new TestClass1(), null,             null, "defaultValue"),
                Arguments.of(new TestClass1(), "_nullValue",     null, "defaultValue"),

                Arguments.of(new TestClass2(), 45,               null, "45"),
                Arguments.of(new TestClass2(), true,             null, "true"),
                Arguments.of(new TestClass2(), false,            null, "false"),
                Arguments.of(new TestClass2(), "45",             null, "45"),
                Arguments.of(new TestClass2(), new JSONObject(), null, "{}"),
                Arguments.of(new TestClass2(), null,             null, ""),
                Arguments.of(new TestClass2(), "_nullValue",     null, "")
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
                Arguments.of(new TestListClass1(), 45,                              new String[] {"45"}),
                Arguments.of(new TestListClass1(), new int[] {44, 45, 46},          new String[] {"44", "45", "46"}),
                Arguments.of(new TestListClass1(), "45",                            new String[] {"45"}),
                Arguments.of(new TestListClass1(), new String[] {"44", "45", "46"}, new String[] {"44", "45", "46"}),
                Arguments.of(new TestListClass1(), null,                            new String[] {"defaultValue1", "defaultValue2"}),
                Arguments.of(new TestListClass1(), "_nullValue",                    new String[] {"defaultValue1", "defaultValue2"}),
                Arguments.of(new TestListClass1(), new String[0],                   new String[] {}),

                Arguments.of(new TestListClass2(), 45,                              new String[] {"45"}),
                Arguments.of(new TestListClass2(), new int[] {44, 45, 46},          new String[] {"44", "45", "46"}),
                Arguments.of(new TestListClass2(), "45",                            new String[] {"45"}),
                Arguments.of(new TestListClass2(), new String[] {"44", "45", "46"}, new String[] {"44", "45", "46"}),
                Arguments.of(new TestListClass2(), null,                            new String[] {}),
                Arguments.of(new TestListClass2(), "_nullValue",                    new String[] {}),
                Arguments.of(new TestListClass2(), new String[0],                   new String[] {})
        );
    }

    @ParameterizedTest
    @MethodSource("getOptionsArrayArguments")
    public void testExtractOptionArray(
            final Object underTest,
            final Object sourceValue,
            final Object[] expectedExtractedValue) throws Throwable {
        super.testExtractOptionArray(
                underTest,
                sourceValue,
                expectedExtractedValue
        );
    }

    static Stream<Arguments> getOptionsArrayArguments() {
        return Stream.of(
                Arguments.of(new TestArrayClass1(), 45,                              new String[] {"45"}),
                Arguments.of(new TestArrayClass1(), new int[] {44, 45, 46},          new String[] {"44", "45", "46"}),
                Arguments.of(new TestArrayClass1(), "45",                            new String[] {"45"}),
                Arguments.of(new TestArrayClass1(), new String[] {"44", "45", "46"}, new String[] {"44", "45", "46"}),
                Arguments.of(new TestArrayClass1(), null,                            new String[] {"defaultValue1", "defaultValue2"}),
                Arguments.of(new TestArrayClass1(), "_nullValue",                    new String[] {"defaultValue1", "defaultValue2"}),
                Arguments.of(new TestArrayClass1(), new String[0],                   new String[] {}),

                Arguments.of(new TestArrayClass2(), 45,                              new String[] {"45"}),
                Arguments.of(new TestArrayClass2(), new int[] {44, 45, 46},          new String[] {"44", "45", "46"}),
                Arguments.of(new TestArrayClass2(), "45",                            new String[] {"45"}),
                Arguments.of(new TestArrayClass2(), new String[] {"44", "45", "46"}, new String[] {"44", "45", "46"}),
                Arguments.of(new TestArrayClass2(), null,                            new String[] {}),
                Arguments.of(new TestArrayClass2(), "_nullValue",                    new String[] {}),
                Arguments.of(new TestArrayClass2(), new String[0],                   new String[] {})
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
                Arguments.of(new TestClass1(),      "defaultValue"),
                Arguments.of(new TestClass2(),      "empty string"),
                Arguments.of(new TestListClass1(),  "[defaultValue1, defaultValue2]"),
                Arguments.of(new TestListClass2(),  "empty list"),
                Arguments.of(new TestArrayClass1(), "[defaultValue1, defaultValue2]"),
                Arguments.of(new TestArrayClass2(), "empty array")
        );
    }

}
