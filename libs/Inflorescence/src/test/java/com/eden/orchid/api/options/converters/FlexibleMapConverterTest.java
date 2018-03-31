package com.eden.orchid.api.options.converters;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class FlexibleMapConverterTest {

    private FlexibleIterableConverter iterableConverter;
    private FlexibleMapConverter underTest;

    @BeforeAll
    static void setupAll() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    @BeforeEach
    void setupTest() {
        iterableConverter = new FlexibleIterableConverter(() -> underTest);
        underTest = new FlexibleMapConverter();
    }

    @Test
    void testResultClass() {
        assertThat(underTest.resultClass(), is(equalTo(Map.class)));
    }

    @Test
    void testNull() {
        String source = null;

        EdenPair<Boolean, Map> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(false)));
        assertThat((Map<Object, String>) result.second, is(not(nullValue())));
        assertThat(result.second.isEmpty(), is(equalTo(true)));
    }

    @Test
    void testSingleItem() {
        String source = "test";

        EdenPair<Boolean, Map> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(false)));
        assertThat((Map<Object, String>) result.second, is(not(nullValue())));
        assertThat((Collection<Object>) result.second.keySet(), containsInAnyOrder(new Object[]{null}));
        assertThat((Collection<String>) result.second.values(), containsInAnyOrder("test"));
    }

    @Test
    void testMap() {
        Map<String, String> source = new HashMap<>();
        source.put("key", "test");

        EdenPair<Boolean, Map> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(true)));
        assertThat((Collection<Object>) result.second.keySet(), containsInAnyOrder(new Object[]{"key"}));
        assertThat((Collection<String>) result.second.values(), containsInAnyOrder("test"));
        assertThat((Map<String, String>) result.second, is(sameInstance(source)));
    }

    @Test
    void testJsonObject() {
        JSONObject source = new JSONObject();
        source.put("key", "test");

        EdenPair<Boolean, Map> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(true)));
        assertThat((Collection<Object>) result.second.keySet(), containsInAnyOrder(new Object[]{"key"}));
        assertThat((Collection<String>) result.second.values(), containsInAnyOrder("test"));
        assertThat((Map<String, String>) result.second, is(not(sameInstance(source))));
    }


}
