package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.converters.ClogStringConverterHelper;
import com.eden.orchid.api.converters.IntegerConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.converters.StringConverterHelper;
import com.eden.orchid.api.options.annotations.IntDefault;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Test(groups = {"unit"})
public class IntOptionExtractorTest {

    private StringConverter stringConverter;
    private IntegerConverter integerConverter;
    private IntOptionExtractor underTest;
    private String optionKey;
    private JSONObject optionsObject;

    private Field field;

    @BeforeMethod
    public void testSetup() throws Throwable {
        Clog.setMinPriority(Clog.Priority.FATAL);
        Set<StringConverterHelper> helpers = new HashSet<>();
        helpers.add(new ClogStringConverterHelper());
        stringConverter = new StringConverter(helpers);
        integerConverter = new IntegerConverter(stringConverter);
        underTest = new IntOptionExtractor(integerConverter);
        optionKey = "optionKey";

        optionsObject = new JSONObject();
    }

// Single Values
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCanHandlePrimitiveValue() throws Throwable {
        field = PrimitiveTestClass.class.getField("emptyDefaultField");

        assertThat(underTest.acceptsClass(int.class), is(true));

        optionsObject.put(optionKey, 11);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(11));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = PrimitiveTestClass.class.getField("emptyDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(0));

        field = PrimitiveTestClass.class.getField("filledDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(11));
    }

    @Test
    public void testCanHandleValueClass() throws Throwable {
        field = ClassTestClass.class.getField("emptyDefaultField");

        assertThat(underTest.acceptsClass(Integer.class), is(true));

        optionsObject.put(optionKey, 11);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(new Integer(11)));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = ClassTestClass.class.getField("emptyDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(new Integer(0)));

        field = ClassTestClass.class.getField("filledDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(new Integer(11)));
    }

// testing classes
//----------------------------------------------------------------------------------------------------------------------

    public class PrimitiveTestClass {

        public int emptyDefaultField;

        @IntDefault(11)
        public int filledDefaultField;
    }

    public class ClassTestClass {

        public Integer emptyDefaultField;

        @IntDefault(11)
        public Integer filledDefaultField;
    }

}
