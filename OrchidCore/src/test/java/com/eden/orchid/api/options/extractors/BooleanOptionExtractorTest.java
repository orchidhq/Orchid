package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.BooleanConverter;
import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.converters.NumberConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.converters.ClogStringConverterHelper;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@Test(groups = {"unit"})
public class BooleanOptionExtractorTest {

    private OrchidContext context;

    private StringConverter stringConverter;
    private LongConverter longConverter;
    private DoubleConverter doubleConverter;
    private NumberConverter numberConverter;
    private BooleanConverter booleanConverter;
    private BooleanOptionExtractor underTest;
    private String optionKey;
    private JSONObject optionsObject;

    private Field field;

    @BeforeMethod
    public void testSetup() throws Throwable {
        Clog.setMinPriority(Clog.Priority.FATAL);
        context = mock(OrchidContext.class);

        stringConverter = new StringConverter(new ClogStringConverterHelper());
        longConverter = new LongConverter(stringConverter);
        doubleConverter = new DoubleConverter(stringConverter);
        numberConverter = new NumberConverter(longConverter, doubleConverter);
        booleanConverter = new BooleanConverter(stringConverter, numberConverter);

        underTest = new BooleanOptionExtractor(booleanConverter);
        optionKey = "optionKey";

        optionsObject = new JSONObject();
    }

    @Test
    public void testCanHandlePrimitiveValue() throws Throwable {
        field = PrimitiveTestClass.class.getField("defaultField");

        assertThat(underTest.acceptsClass(boolean.class), is(true));

        optionsObject.put(optionKey, Boolean.TRUE);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(true));

        optionsObject.put(optionKey, Boolean.FALSE);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(false));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = PrimitiveTestClass.class.getField("defaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(false));

        field = PrimitiveTestClass.class.getField("trueDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(true));

        field = PrimitiveTestClass.class.getField("falseDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(false));
    }

    @Test
    public void testCanHandleValueClass() throws Throwable {
        field = ClassTestClass.class.getField("defaultField");

        assertThat(underTest.acceptsClass(Boolean.class), is(true));

        optionsObject.put(optionKey, true);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(true));

        optionsObject.put(optionKey, false);
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(false));

        optionsObject.remove(optionKey);
        assertThat(optionsObject.has(optionKey), is(false));

        field = ClassTestClass.class.getField("defaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(false));

        field = ClassTestClass.class.getField("trueDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(true));

        field = ClassTestClass.class.getField("falseDefaultField");
        assertThat(underTest.getOption(field, optionsObject, optionKey), is(false));
    }

    @Test
    public void testCannotHandleVectors() throws Throwable {
        field = ClassTestClass.class.getField("defaultField");

        optionsObject.put(optionKey, true);
        assertThat(underTest.getList(field, optionsObject, optionKey), is(nullValue()));

        assertThat(underTest.getArray(field, optionsObject, optionKey), is(nullValue()));
    }



// testing classes
//----------------------------------------------------------------------------------------------------------------------

    public class PrimitiveTestClass {

        public boolean defaultField;

        @BooleanDefault(true)
        public boolean trueDefaultField;

        @BooleanDefault(false)
        public boolean falseDefaultField;
    }

    public class ClassTestClass {

        public Boolean defaultField;

        @BooleanDefault(true)
        public Boolean trueDefaultField;

        @BooleanDefault(false)
        public Boolean falseDefaultField;
    }

}
