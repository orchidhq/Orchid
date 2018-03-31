package com.eden.orchid.api.options.converters;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class FlexibleIterableConverterTest {

    private FlexibleMapConverter mapConverter;
    private FlexibleIterableConverter underTest;

    @BeforeAll
    static void setupAll() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    @BeforeEach
    void setupTest() {
        mapConverter = new FlexibleMapConverter();
        underTest = new FlexibleIterableConverter(() -> mapConverter);
    }

    @Test
    void testResultClass() {
        assertThat(underTest.resultClass(), is(equalTo(Iterable.class)));
    }

    @Test
    void testNull() {
        String source = null;

        EdenPair<Boolean, Iterable> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(false)));
        assertThat((Iterable<String>) result.second, is(not(nullValue())));
        assertThat(result.second.iterator().hasNext(), is(equalTo(false)));
    }

    @Test
    void testSingleItem() {
        String source = "test";

        EdenPair<Boolean, Iterable> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(false)));
        assertThat((Iterable<String>) result.second, containsInAnyOrder("test"));
    }

    @Test
    void testArray() {
        String[] source = new String[] {"test"};

        EdenPair<Boolean, Iterable> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(true)));
        assertThat((Iterable<String>) result.second, containsInAnyOrder("test"));
        assertThat((Iterable<String>) result.second, is(not(sameInstance(source))));
    }

    @Test
    void testSet() {
        Set<String> source = new HashSet<>();
        source.add("test");

        EdenPair<Boolean, Iterable> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(true)));
        assertThat((Iterable<String>) result.second, containsInAnyOrder("test"));
        assertThat((Iterable<String>) result.second, is(sameInstance(source)));
    }

    @Test
    void testList() {
        List<String> source = new ArrayList<>();
        source.add("test");

        EdenPair<Boolean, Iterable> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(true)));
        assertThat((Iterable<String>) result.second, containsInAnyOrder("test"));
        assertThat((Iterable<String>) result.second, is(sameInstance(source)));
    }

    @Test
    void testAbstractIterable() {
        Iterable<String> source = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Collections.singletonList("test").iterator();
            }
        };

        EdenPair<Boolean, Iterable> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(true)));
        assertThat((Iterable<String>) result.second, containsInAnyOrder("test"));
        assertThat((Iterable<String>) result.second, is(sameInstance(source)));
    }

    @Test
    void testMapOfStrings() {
        Map<String, String> source = new HashMap<>();
        source.put("key", "test");

        EdenPair<Boolean, Iterable> result = underTest.convert(source);

        assertThat(result.first, is(equalTo(false)));
        assertThat((Iterable<String>) result.second, containsInAnyOrder("test"));
    }

    @Test
    void testMapOfMaps() {
        Map<String, Map<String, String>> source = new HashMap<>();
        Map<String, String> object = new HashMap<>();
        object.put("key", "test");
        source.put("otherKey", object);

        EdenPair<Boolean, Iterable> result = underTest.convert(source, "newKey");

        assertThat(result.first, is(equalTo(false)));
        assertThat((Iterable<Map<String, String>>) result.second, is(notNullValue()));
        assertThat(((Iterable<Map<String, String>>) result.second).iterator().hasNext(), is(equalTo(true)));

        Map<String, String> newObject = ((Iterable<Map<String, String>>) result.second).iterator().next();
        assertThat(newObject.get("key"), is(equalTo("test")));
        assertThat(newObject.get("newKey"), is(equalTo("otherKey")));
    }

    @Test
    void testMapOfJsonObjects() {
        Map<String, JSONObject> source = new HashMap<>();
        JSONObject object = new JSONObject();
        object.put("key", "test");
        source.put("otherKey", object);

        EdenPair<Boolean, Iterable> result = underTest.convert(source, "newKey");

        assertThat(result.first, is(equalTo(false)));
        assertThat((Iterable<JSONObject>) result.second, is(notNullValue()));
        assertThat(((Iterable<JSONObject>) result.second).iterator().hasNext(), is(equalTo(true)));

        Map<String, String> newObject = ((Iterable<Map<String, String>>) result.second).iterator().next();
        assertThat(newObject.get("key"), is(equalTo("test")));
        assertThat(newObject.get("newKey"), is(equalTo("otherKey")));
    }

}
