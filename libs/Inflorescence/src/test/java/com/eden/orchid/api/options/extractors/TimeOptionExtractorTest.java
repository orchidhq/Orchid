package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.DateTimeConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.converters.TimeConverter;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class TimeOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass { @Option public LocalTime testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        DateTimeConverter dateTimeConverter = new DateTimeConverter(stringConverter);
        TimeConverter timeConverter = new TimeConverter(dateTimeConverter);

        setupTest(new TimeOptionExtractor(timeConverter), stringConverter, dateTimeConverter, timeConverter);
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("getOptionsArguments")
    public void testExtractOption(
            final Object underTest,
            final Object sourceValue,
            final Object expectedOriginalValue,
            final LocalTime expectedExtractedValue) throws Throwable {
        String optionName = "testValue";

        final JSONObject options = new JSONObject();
        if(sourceValue != null) {
            if(sourceValue.toString().equals("_nullValue")) {
                options.put(optionName, (String) null);
            }
            else {
                options.put(optionName, sourceValue);
            }
        }

        LocalTime time = (LocalTime) underTest.getClass().getField(optionName).get(underTest);

        assertThat(time, is(equalTo(expectedOriginalValue)));
        extractor.extractOptions(underTest, options);

        time = ((LocalTime) underTest
                .getClass()
                .getField(optionName)
                .get(underTest))
                .withNano(0)
                .withSecond(0);
        assertThat(time, is(equalTo(expectedExtractedValue.withNano(0).withSecond(0))));
    }

    static Stream<Arguments> getOptionsArguments() throws Throwable {
        return Stream.of(
                Arguments.of(new TestClass(), "2018-01-01T08:30:00",                  null, LocalTime.of(8, 30, 0)),
                Arguments.of(new TestClass(), LocalDate.of(2018, 1, 1),               null, LocalTime.now()),
                Arguments.of(new TestClass(), LocalDateTime.of(2018, 1, 1, 8, 30, 0), null, LocalTime.of(8, 30, 0)),
                Arguments.of(new TestClass(), "now",                                  null, LocalTime.now()),
                Arguments.of(new TestClass(), null,                                   null, LocalTime.now()),
                Arguments.of(new TestClass(), "_nullValue",                           null, LocalTime.now()),

                Arguments.of(new TestClass(), "now",                                  null, LocalTime.now()),
                Arguments.of(new TestClass(), "today",                                null, LocalTime.of(0, 0, 0)),
                Arguments.of(new TestClass(), "tomorrow",                             null, LocalTime.of(0, 0, 0)),
                Arguments.of(new TestClass(), "yesterday",                            null, LocalTime.of(0, 0, 0))
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
                Arguments.of(new TestClass(), "now (HH:MM:SS)")
        );
    }

}
