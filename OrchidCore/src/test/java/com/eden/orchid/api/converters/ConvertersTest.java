package com.eden.orchid.api.converters;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups={"services", "unit"})
public final class ConvertersTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;

    private BooleanConverter booleanConverter;
    private NumberConverter numberConverter;
    private LongConverter longConverter;
    private DoubleConverter doubleConverter;
    private IntegerConverter integerConverter;
    private FloatConverter floatConverter;

    @BeforeMethod
    public void testSetup() {
        floatConverter = new FloatConverter();
        integerConverter = new IntegerConverter();
        doubleConverter = new DoubleConverter();
        longConverter = new LongConverter();
        numberConverter = new NumberConverter(longConverter, doubleConverter);
        booleanConverter = new BooleanConverter(numberConverter);
    }

    @Test
    public void testFloatConverter() throws Throwable {
        assertThat(floatConverter.resultClass(), is(equalTo(Float.class)));

        assertThat(floatConverter.convert(12.2f).first, is(true));
        assertThat(floatConverter.convert(12.2f).second, is(equalTo(12.2f)));

        assertThat(floatConverter.convert(12.2).first, is(true));
        assertThat(floatConverter.convert(12.2).second, is(equalTo(12.2f)));

        assertThat(floatConverter.convert("12.2f").first, is(true));
        assertThat(floatConverter.convert("12.2f").second, is(equalTo(12.2f)));

        assertThat(floatConverter.convert("12.2").first, is(true));
        assertThat(floatConverter.convert("12.2").second, is(equalTo(12.2f)));

        assertThat(floatConverter.convert(new Object()).first, is(false));
        assertThat(floatConverter.convert(new Object()).second, is(equalTo(0.0f)));

        assertThat(floatConverter.convert(null).first, is(false));
        assertThat(floatConverter.convert(null).second, is(equalTo(0.0f)));
    }

    @Test
    public void testDoubleConverter() throws Throwable {
        assertThat(doubleConverter.resultClass(), is(equalTo(Double.class)));

        assertThat(doubleConverter.convert(12.2f).first, is(true));
        assertThat(doubleConverter.convert(12.2f).second, is(equalTo(12.2)));

        assertThat(doubleConverter.convert(12.2).first, is(true));
        assertThat(doubleConverter.convert(12.2).second, is(equalTo(12.2)));

        assertThat(doubleConverter.convert("12.2f").first, is(true));
        assertThat(doubleConverter.convert("12.2f").second, is(equalTo(12.2)));

        assertThat(doubleConverter.convert("12.2").first, is(true));
        assertThat(doubleConverter.convert("12.2").second, is(equalTo(12.2)));

        assertThat(doubleConverter.convert(new Object()).first, is(false));
        assertThat(doubleConverter.convert(new Object()).second, is(equalTo(0.0)));

        assertThat(doubleConverter.convert(null).first, is(false));
        assertThat(doubleConverter.convert(null).second, is(equalTo(0.0)));
    }

    @Test
    public void testIntegerConverter() throws Throwable {
        assertThat(integerConverter.resultClass(), is(equalTo(Integer.class)));

        assertThat(integerConverter.convert(12).first, is(true));
        assertThat(integerConverter.convert(12).second, is(equalTo(12)));

        assertThat(integerConverter.convert("12").first, is(true));
        assertThat(integerConverter.convert("12").second, is(equalTo(12)));

        assertThat(integerConverter.convert(new Object()).first, is(false));
        assertThat(integerConverter.convert(new Object()).second, is(equalTo(0)));

        assertThat(integerConverter.convert(null).first, is(false));
        assertThat(integerConverter.convert(null).second, is(equalTo(0)));
    }

    @Test
    public void testLongConverter() throws Throwable {
        assertThat(longConverter.resultClass(), is(equalTo(Long.class)));

        assertThat(longConverter.convert(12L).first, is(true));
        assertThat(longConverter.convert(12L).second, is(equalTo(12L)));

        assertThat(longConverter.convert(12).first, is(true));
        assertThat(longConverter.convert(12).second, is(equalTo(12L)));

        assertThat(longConverter.convert("12").first, is(true));
        assertThat(longConverter.convert("12").second, is(equalTo(12L)));

        assertThat(longConverter.convert(new Object()).first, is(false));
        assertThat(longConverter.convert(new Object()).second, is(equalTo(0L)));

        assertThat(longConverter.convert(null).first, is(false));
        assertThat(longConverter.convert(null).second, is(equalTo(0L)));
    }

    @Test
    public void testNumberConverter() throws Throwable {
        assertThat(numberConverter.resultClass(), is(equalTo(Number.class)));

        assertThat(numberConverter.convert(12L).first, is(true));
        assertThat(numberConverter.convert(12L).second, is(equalTo(12L)));

        assertThat(numberConverter.convert(12).first, is(true));
        assertThat(numberConverter.convert(12).second, is(equalTo(12L)));

        assertThat(numberConverter.convert(12.2).first, is(true));
        assertThat(numberConverter.convert(12.2).second, is(equalTo(12.2)));

        assertThat(numberConverter.convert(12.2f).first, is(true));
        assertThat(numberConverter.convert(12.2f).second, is(equalTo(12.2)));

        assertThat(numberConverter.convert("12").first, is(true));
        assertThat(numberConverter.convert("12").second, is(equalTo(12L)));

        assertThat(numberConverter.convert("12.2").first, is(true));
        assertThat(numberConverter.convert("12.2").second, is(equalTo(12.2)));

        assertThat(numberConverter.convert(new Object()).first, is(false));
        assertThat(numberConverter.convert(new Object()).second, is(equalTo(0)));

        assertThat(numberConverter.convert(null).first, is(false));
        assertThat(numberConverter.convert(null).second, is(equalTo(0)));
    }

    @Test
    public void testBooleanConverter() throws Throwable {
        assertThat(booleanConverter.resultClass(), is(equalTo(Boolean.class)));

        assertThat(booleanConverter.convert(true).first, is(true));
        assertThat(booleanConverter.convert(true).second, is(equalTo(true)));

        assertThat(booleanConverter.convert(false).first, is(true));
        assertThat(booleanConverter.convert(false).second, is(equalTo(false)));

        assertThat(booleanConverter.convert("true").first, is(true));
        assertThat(booleanConverter.convert("true").second, is(equalTo(true)));

        assertThat(booleanConverter.convert("false").first, is(true));
        assertThat(booleanConverter.convert("false").second, is(equalTo(false)));

        assertThat(booleanConverter.convert(12L).first, is(true));
        assertThat(booleanConverter.convert(12L).second, is(equalTo(true)));

        assertThat(booleanConverter.convert(12).first, is(true));
        assertThat(booleanConverter.convert(12).second, is(equalTo(true)));

        assertThat(booleanConverter.convert(12.2).first, is(true));
        assertThat(booleanConverter.convert(12.2).second, is(equalTo(true)));

        assertThat(booleanConverter.convert(12.2f).first, is(true));
        assertThat(booleanConverter.convert(12.2f).second, is(equalTo(true)));

        assertThat(booleanConverter.convert("12").first, is(true));
        assertThat(booleanConverter.convert("12").second, is(equalTo(true)));

        assertThat(booleanConverter.convert("12.2").first, is(true));
        assertThat(booleanConverter.convert("12.2").second, is(equalTo(true)));

        assertThat(booleanConverter.convert(0).first, is(true));
        assertThat(booleanConverter.convert(0).second, is(equalTo(false)));

        assertThat(booleanConverter.convert(0.0).first, is(true));
        assertThat(booleanConverter.convert(0.0).second, is(equalTo(false)));

        assertThat(booleanConverter.convert("0.0").first, is(true));
        assertThat(booleanConverter.convert("0.0").second, is(equalTo(false)));

        assertThat(booleanConverter.convert(new Object()).first, is(false));
        assertThat(booleanConverter.convert(new Object()).second, is(equalTo(false)));

        assertThat(booleanConverter.convert(null).first, is(true));
        assertThat(booleanConverter.convert(null).second, is(equalTo(false)));
    }


}
