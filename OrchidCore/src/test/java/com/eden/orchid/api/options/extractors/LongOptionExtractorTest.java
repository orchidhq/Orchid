package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.options.annotations.LongDefault;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups = {"unit"})
public class LongOptionExtractorTest {

    private LongOptionExtractor underTest;
    private String optionKey;
    private JSONObject optionsObject;

    private Field field;

    @BeforeMethod
    public void testSetup() throws Throwable {
        Clog.setMinPriority(Clog.Priority.FATAL);
        underTest = new LongOptionExtractor();
        optionKey = "optionKey";

        optionsObject = new JSONObject();
    }

// Single Values
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCanHandlePrimitiveValue() throws Throwable {
        field = PrimitiveTestClass.class.getField("emptyDefaultField");

        assertThat(underTest.acceptsClass(long.class), is(true));

        optionsObject.put(optionKey, 11L);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(11L));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = PrimitiveTestClass.class.getField("emptyDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(0L));

        field = PrimitiveTestClass.class.getField("filledDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(11L));
    }

    @Test
    public void testCanHandleValueClass() throws Throwable {
        field = ClassTestClass.class.getField("emptyDefaultField");

        assertThat(underTest.acceptsClass(Long.class), is(true));

        optionsObject.put(optionKey, 11L);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(new Long(11L)));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = ClassTestClass.class.getField("emptyDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(new Long(0L)));

        field = ClassTestClass.class.getField("filledDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(new Long(11L)));
    }

// testing classes
//----------------------------------------------------------------------------------------------------------------------

    public class PrimitiveTestClass {

        public long emptyDefaultField;

        @LongDefault(11L)
        public long filledDefaultField;
    }

    public class ClassTestClass {

        public Long emptyDefaultField;

        @LongDefault(11L)
        public Long filledDefaultField;
    }

}
