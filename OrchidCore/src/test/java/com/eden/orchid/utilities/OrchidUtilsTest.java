package com.eden.orchid.utilities;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public final class OrchidUtilsTest {

    @BeforeEach
    public void testSetup() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    @ParameterizedTest
    @CsvSource({
            "https://www.example.com, one/two,  https://www.example.com/one/two",
            "https://www.example.com, /one/two, https://www.example.com/one/two",
    })
    public void applyBaseUrlTest(String baseUrl, String input, String expected) {
        OrchidContext context = mock(OrchidContext.class);
        when(context.getBaseUrl()).thenReturn(baseUrl);

        String output = OrchidUtils.applyBaseUrl(context, input);
        assertThat(output, is(equalTo(expected)));
    }

    @ParameterizedTest
    @CsvSource({
            "/one/two,       /one/two/three/four/five,        three/four/five",
            "/one/two/,      /one/two/three/four/five,        three/four/five",
            "C:\\one\\two,   C:\\one\\two\\three\\four\\five, three\\four\\five",
            "C:\\one\\two\\, C:\\one\\two\\three\\four\\five, three\\four\\five",
            "one\\two,       one\\two\\three\\four\\five,     three\\four\\five",
            "one\\two\\,     one\\two\\three\\four\\five,     three\\four\\five",
    })
    public void getRelativeFilenameTest(String baseDir, String input, String expected) {
        String output = OrchidUtils.getRelativeFilename(input, baseDir);
        assertThat(output, is(equalTo(expected)));
    }

    @ParameterizedTest
    @CsvSource({
            "false, one/two,      one/two",
            "false, /one/two,     one/two",
            "true,  C:\\one\\two, C:/one/two",
            "true,  \\one\\two,   one/two",
            "true,  one\\two,     one/two",
    })
    public void normalizePathTest(boolean isWindows, String input, String expected) {
        OrchidUtils.isWindows = isWindows;
        String output = OrchidUtils.normalizePath(input);

        assertThat(output, is(equalTo(expected)));
    }

    @ParameterizedTest
    @MethodSource("parseCommandLineArgsValues")
    public void parseCommandLineArgsTest(String[] input, Map<String, String[]> expected) {
        Map<String, String[]> output = OrchidUtils.parseCommandLineArgs(input);

        assertThat(output.keySet(), containsInAnyOrder(expected.keySet().toArray()));

        for(String key : expected.keySet()) {
            assertThat(output.get(key), is(arrayContaining(expected.get(key))));
        }
    }
    public static Stream<Arguments> parseCommandLineArgsValues() {
        return Stream.of(
                Arguments.of(
                        new String[] {"-key1 val1"},
                        Collections.unmodifiableMap(new HashMap<String, String[]>() {{
                            put("-key1", new String[]{"-key1", "val1"});
                        }})
                ),
                Arguments.of(
                        new String[] {"-key2 val1 val2 val3"},
                        Collections.unmodifiableMap(new HashMap<String, String[]>() {{
                            put("-key2", new String[]{"-key2", "val1", "val2", "val3"});
                        }})
                ),
                Arguments.of(
                        new String[] {"-key1 val1", "-key2 val1 val2 val3"},
                        Collections.unmodifiableMap(new HashMap<String, String[]>() {{
                            put("-key1", new String[]{"-key1", "val1"});
                            put("-key2", new String[]{"-key2", "val1", "val2", "val3"});
                        }})
                )
        );
    }

    @ParameterizedTest
    @MethodSource("parseCommandArgsValues")
    public void parseCommandArgsTest(String input, String[] paramKeys, Map<String, Object> expected) {
        Map<String, Object> output = OrchidUtils.parseCommandArgs(input, paramKeys).toMap();

        assertThat(output.keySet(), containsInAnyOrder(expected.keySet().toArray()));

        for(String key : expected.keySet()) {
            Object result = expected.get(key);
            if(result.getClass().isArray()) {
                assertThat((Object[]) output.get(key), is(arrayContaining((Object[]) result)));
            }
            else {
                assertThat(output.get(key), is(equalTo(result)));
            }
        }
    }
    public static Stream<Arguments> parseCommandArgsValues() {
        return Stream.of(
                Arguments.of(
                        "false true",
                        new String[] {"key1", "key2"},
                        Collections.unmodifiableMap(new HashMap<String, Object>() {{
                            put("key1", "false");
                            put("key2", "true");
                        }})
                ),
                Arguments.of(
                        "-- -key1 false -key2 true",
                        new String[] {"key1", "key2"},
                        Collections.unmodifiableMap(new HashMap<String, Object>() {{
                            put("key1", "false");
                            put("key2", "true");
                        }})
                ),
                Arguments.of(
                        "false true -- -key3 val1 val2 val3",
                        new String[] {"key1", "key2"},
                        Collections.unmodifiableMap(new HashMap<String, Object>() {{
                            put("key1", "false");
                            put("key2", "true");
                            put("key3", new String[]{"val1", "val2", "val3"});
                        }})
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "one-two, one-two",
            "one_two, one_two",
            "one/two, one/two",
            "one two, one-two",
            "ONE-TWO, one-two",
            "ONE_TWO, one_two",
            "ONE/TWO, one/two",
            "ONE TWO, one-two",
    })
    public void toSlugTest(String input, String expected) {
        String output = OrchidUtils.toSlug(input);

        assertThat(output, is(equalTo(expected)));
    }

    @ParameterizedTest
    @CsvSource({
            "http://www.example.com,  true",
            "https://www.example.com, true",
            "www.example.com,         false",
    })
    public void isExternal(String input, boolean expected) {
        boolean output = OrchidUtils.isExternal(input);

        assertThat(output, is(equalTo(expected)));
    }

    @ParameterizedTest
    @MethodSource("firstValues")
    public void firstTest(@Nullable Object[] input, @Nullable Object expected) {
        Object output = null;
        if(input != null) {
            output = OrchidUtils.first(Arrays.asList(input));
        }
        else {
            output = OrchidUtils.first((List<Object>) null);
        }

        if (expected == null) {
            assertThat(output, is(nullValue()));
        }
        else {
            assertThat(output, is(equalTo(expected)));
        }
    }
    public static Stream<Arguments> firstValues() {
        return Stream.of(
                Arguments.of(new String[] {"key1", "key2"}, "key1"),
                Arguments.of(new String[] {}, null),
                Arguments.of(new Integer[] {1, 2}, 1),
                Arguments.of(new Integer[] {}, null),
                Arguments.of(null, null)
        );
    }

// Deprecated methods tests
//----------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({
            "oneTwoThree, One Two Three",
            "OneTwoThree, One Two Three",
    })
    public void camelcaseToTitleCaseTest(String input, String expected) {
        String output = OrchidUtils.camelcaseToTitleCase(input);

        assertThat(output, is(equalTo(expected)));
    }

}
