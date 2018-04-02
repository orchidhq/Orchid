package com.eden.orchid.api.options.converters;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.converters.IntegerConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.converters.TypeConverter;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ConvertersTest {

    private Converters underTest;

    @BeforeEach
    void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        IntegerConverter integerConverter = new IntegerConverter(stringConverter);
        DoubleConverter doubleConverter = new DoubleConverter(stringConverter);

        Set<TypeConverter> converters = new HashSet<>();
        converters.add(stringConverter);
        converters.add(integerConverter);
        converters.add(doubleConverter);

        underTest = new Converters(converters);
    }

    @ParameterizedTest
    @MethodSource("testConvertToStringValues")
    void testConvertToString(
            final Object input,
            final boolean expectedStatus,
            final Object expectedValue,
            Class<?> targetClass
    ) {
        EdenPair<Boolean, ?> result = underTest.convert(input, targetClass);
        assertThat(result.first, is(equalTo(expectedStatus)));
        assertThat(result.second, is(equalTo(expectedValue)));
    }

    static Stream<Arguments> testConvertToStringValues() {
        return Stream.of(

                // test with StringConverter
                Arguments.of("45",             true, "45",  String.class),
                Arguments.of(45,               true, "45",  String.class),
                Arguments.of(new JSONObject(), true, "{}",  String.class),
                Arguments.of(null,             true, "",    String.class),

                // test with IntegerConverter
                Arguments.of(45,               true,  45,   Integer.class),
                Arguments.of("45",             true,  45,   Integer.class),
                Arguments.of(new JSONObject(), false, 0,    Integer.class),
                Arguments.of(null,             false, 0,    Integer.class),

                // test with DoubleConverter
                Arguments.of(45.1,             true,  45.1, Double.class),
                Arguments.of("45.1",           true,  45.1, Double.class),
                Arguments.of(new JSONObject(), false, 0.0,  Double.class),
                Arguments.of(null,             false, 0.0,  Double.class),

                // test where it can't find an appropriate converter
                Arguments.of(45.1,             false, null, Object.class),
                Arguments.of("45.1",           false, null, Object.class),
                Arguments.of(new JSONObject(), false, null, Object.class),
                Arguments.of(null,             false, null, Object.class)
        );
    }

}
