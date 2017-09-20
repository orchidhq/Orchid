package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.converters.FloatConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.FloatDefault;
import com.eden.orchid.api.converters.ClogStringConverterHelper;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups = {"unit"})
public class FloatOptionExtractorTest {

    private StringConverter stringConverter;
    private FloatConverter floatConverter;
    private FloatOptionExtractor underTest;
    private String optionKey;
    private JSONObject optionsObject;

    private Field field;

    @BeforeMethod
    public void testSetup() throws Throwable {
        Clog.setMinPriority(Clog.Priority.FATAL);
        stringConverter = new StringConverter(new ClogStringConverterHelper());
        floatConverter = new FloatConverter(stringConverter);
        underTest = new FloatOptionExtractor(floatConverter);
        optionKey = "optionKey";

        optionsObject = new JSONObject();
    }

// Single Values
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCanHandlePrimitiveValue() throws Throwable {
        field = PrimitiveTestClass.class.getField("emptyDefaultField");

        assertThat(underTest.acceptsClass(float.class), is(true));

        optionsObject.put(optionKey, 11.1f);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(11.1f));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = PrimitiveTestClass.class.getField("emptyDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(0.0f));

        field = PrimitiveTestClass.class.getField("filledDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(11.1f));
    }

    @Test
    public void testCanHandleValueClass() throws Throwable {
        field = ClassTestClass.class.getField("emptyDefaultField");

        assertThat(underTest.acceptsClass(Float.class), is(true));

        optionsObject.put(optionKey, 11.1f);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(new Float(11.1f)));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = ClassTestClass.class.getField("emptyDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(new Float(0.0f)));

        field = ClassTestClass.class.getField("filledDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(new Float(11.1f)));
    }

// testing classes
//----------------------------------------------------------------------------------------------------------------------

    class PrimitiveTestClass {

        public float emptyDefaultField;

        @FloatDefault(11.1f)
        public float filledDefaultField;
    }

    public class ClassTestClass {

        public Float emptyDefaultField;

        @FloatDefault(11.1f)
        public Float filledDefaultField;
    }

}
