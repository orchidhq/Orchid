package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.converters.ClogStringConverterHelper;
import com.eden.orchid.api.converters.IntegerConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.converters.StringConverterHelper;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.options.extractors.IntOptionExtractor;
import com.eden.orchid.api.options.extractors.StringOptionExtractor;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ExtractorTest {

    public static class ParentTestOptionsClass {

        @Option @StringDefault("default string")
        public String parentStringOption;

        @Option @IntDefault(5)
        public int parentIntOption;

    }

    public static class TestOptionsClass extends ParentTestOptionsClass {

        @Option @StringDefault("default string")
        public String stringOption;

        @Option @IntDefault(5)
        public int intOption;

        @Option("beanSetter")
        private String beanSetterValue;

        public void setBeanSetter(String value) {
            this.beanSetterValue = "setter value";
        }

        public String getBeanSetter() {
            return this.beanSetterValue;
        }

    }

    private Extractor extractor;
    private TestOptionsClass testOptionsClass;

    @BeforeAll
    static void setupAll() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    @BeforeEach
    void setupTest() {
        testOptionsClass = new TestOptionsClass();

        Set<StringConverterHelper> helpers = new HashSet<>();
        helpers.add(new ClogStringConverterHelper());
        StringConverter stringConverter = new StringConverter(helpers);

        Set<OptionExtractor> extractors = new HashSet<>();

        extractors.add(new StringOptionExtractor(stringConverter));
        extractors.add(new IntOptionExtractor(new IntegerConverter(stringConverter)));

        extractor = new Extractor(extractors, null) {
            @Override
            protected <T> T getInstance(Class<T> clazz) {
                return null;
            }
        };
    }

    @ParameterizedTest
    @MethodSource("getOptionsArguments")
    void testExtractStringOption(
            final String optionName,
            final boolean getterIsMethod,
            final Object sourceValue,
            final Object expectedOriginalValue,
            final Object expectedExtractedValue) throws Throwable {

        String s = "{" + optionName + ": " + sourceValue + "}";

        final JSONObject options = new JSONObject(s);

        Object actualOriginalValue = (getterIsMethod)
                ? testOptionsClass.getClass().getMethod("get" + optionName.substring(0, 1).toUpperCase() + optionName.substring(1)).invoke(testOptionsClass)
                : testOptionsClass.getClass().getField(optionName).get(testOptionsClass);
        assertThat(actualOriginalValue, is(equalTo(expectedOriginalValue)));

        extractor.extractOptions(testOptionsClass, options);

        Object actualExtractedValue = (getterIsMethod)
                ? testOptionsClass.getClass().getMethod("get" + optionName.substring(0, 1).toUpperCase() + optionName.substring(1)).invoke(testOptionsClass)
                : testOptionsClass.getClass().getField(optionName).get(testOptionsClass);
        assertThat(actualExtractedValue, is(equalTo(expectedExtractedValue)));
    }

    static Stream<Arguments> getOptionsArguments() {
        return Stream.of(
                Arguments.of("stringOption",       false, null,             null, "default string"),
                Arguments.of("stringOption",       false, "'string value'", null, "string value"),
                Arguments.of("intOption",          false, null,             0,    5),
                Arguments.of("intOption",          false, 10,               0,    10),
                Arguments.of("parentStringOption", false, null,             null, "default string"),
                Arguments.of("parentStringOption", false, "'string value'", null, "string value"),
                Arguments.of("parentIntOption",    false, null,             0,    5),
                Arguments.of("parentIntOption",    false, 10,               0,    10),
                Arguments.of("beanSetter",         true,  "passed value",   null, "setter value")
        );
    }

}
