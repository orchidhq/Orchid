package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.DateTimeConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Stream;

public class DateOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass { @Option public LocalDate testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        DateTimeConverter dateTimeConverter = new DateTimeConverter(stringConverter);

        setupTest(new DateOptionExtractor(dateTimeConverter), stringConverter, dateTimeConverter);
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

    static Stream<Arguments> getOptionsArguments() throws Throwable {
        return Stream.of(
                Arguments.of(new TestClass(), "2018-01-01",                           null, LocalDate.of(2018, 1, 1)),
                Arguments.of(new TestClass(), "2018-01-01T08:30:00",                  null, LocalDate.of(2018, 1, 1)),
                Arguments.of(new TestClass(), LocalDate.of(2018, 1, 1),               null, LocalDate.of(2018, 1, 1)),
                Arguments.of(new TestClass(), LocalDateTime.of(2018, 1, 1, 0, 30, 0), null, LocalDate.of(2018, 1, 1)),
                Arguments.of(new TestClass(), "now",                                  null, LocalDate.now()),
                Arguments.of(new TestClass(), null,                                   null, LocalDate.now()),
                Arguments.of(new TestClass(), "_nullValue",                           null, LocalDate.now())
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
                Arguments.of(new TestClass(), "now (yyyy-mm-dd)")
        );
    }

}
