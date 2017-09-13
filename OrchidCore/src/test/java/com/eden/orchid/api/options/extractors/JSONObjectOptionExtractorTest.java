package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups = {"unit"})
public class JSONObjectOptionExtractorTest {

    private JSONObjectOptionExtractor underTest;
    private String optionKey;
    private JSONObject optionsObject;

    private Field field;

    @BeforeMethod
    public void testSetup() throws Throwable {
        Clog.setMinPriority(Clog.Priority.FATAL);
        underTest = new JSONObjectOptionExtractor();
        optionKey = "optionKey";

        optionsObject = new JSONObject();
    }

// Single Values
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCanHandleJSONArray() throws Throwable {
        assertThat(underTest.acceptsClass(JSONObject.class), is(true));
    }

// testing classes
//----------------------------------------------------------------------------------------------------------------------

    public class ClassTestClass {

    }

}
