package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.options.annotations.StringDefault;
import org.hamcrest.collection.IsArrayContainingInAnyOrder;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups = {"unit"})
public class StringOptionExtractorTest {

    private StringOptionExtractor underTest;
    private String optionKey;
    private JSONObject optionsObject;

    private Field field;

    @BeforeMethod
    public void testSetup() throws Throwable {
        Clog.setMinPriority(Clog.Priority.FATAL);
        underTest = new StringOptionExtractor();
        optionKey = "optionKey";

        optionsObject = new JSONObject();
    }

// Single Values
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCanHandleValueClass() throws Throwable {
        field = ClassTestClass.class.getField("emptyDefaultField");

        assertThat(underTest.acceptsClass(String.class), is(true));

        optionsObject.put(optionKey, "11");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(equalTo("11")));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = ClassTestClass.class.getField("emptyDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(nullValue()));

        field = ClassTestClass.class.getField("filledDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(equalTo("11")));
    }

    @Test
    public void testCanHandleArray() throws Throwable {
        Clog.setMinPriority(Clog.Priority.VERBOSE);

        field = ArrayTestClass.class.getField("emptyDefaultField");

        assertThat(underTest.acceptsClass(String[].class), is(true));

        optionsObject.put(optionKey, new String[] {"11", "12", "13"});
        assertThat(underTest.getArray(field, optionsObject, optionKey), IsArrayContainingInAnyOrder.arrayContainingInAnyOrder("11", "12", "13"));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = ArrayTestClass.class.getField("emptyDefaultField");
        assertThat(underTest.getArray(field, optionsObject, optionKey), is(emptyArray()));
    }

    @Test
    public void testCanHandleList() throws Throwable {
        field = ListTestClass.class.getField("emptyDefaultField");

        assertThat(underTest.acceptsClass(String[].class), is(true));

        optionsObject.put(optionKey, Collections.singletonList("11"));
        assertThat(underTest.getList(field, optionsObject, optionKey), containsInAnyOrder("11"));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = ListTestClass.class.getField("emptyDefaultField");
        assertThat(underTest.getList(field, optionsObject, optionKey), hasSize(equalTo(0)));
    }

// testing classes
//----------------------------------------------------------------------------------------------------------------------

    public class ClassTestClass {

        public String emptyDefaultField;

        @StringDefault("11")
        public String filledDefaultField;
    }

    public class ArrayTestClass {

        public String[] emptyDefaultField;

    }

    public class ListTestClass {

        public List<String> emptyDefaultField;

    }

}
