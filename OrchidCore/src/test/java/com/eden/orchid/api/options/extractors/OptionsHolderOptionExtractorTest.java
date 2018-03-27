package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.options.OptionsHolder;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups = {"unit"})
public class OptionsHolderOptionExtractorTest {

    private OrchidContext context;
    private OptionsHolderOptionExtractor underTest;
    private String optionKey;
    private JSONObject optionsObject;

    private Field field;

    private OptionsExtractor extractor;

    @BeforeMethod
    public void testSetup() throws Throwable {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);

        context = mock(OrchidContext.class);
        extractor = mock(OptionsExtractor.class);

        underTest = new OptionsHolderOptionExtractor(() -> extractor, () -> context);
        optionKey = "optionKey";

        optionsObject = new JSONObject();
    }

// Single Values
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCanHandleJSONArray() throws Throwable {
        assertThat(underTest.acceptsClass(OptionsHolder.class), is(true));
    }

// testing classes
//----------------------------------------------------------------------------------------------------------------------

    public class ClassTestClass {

    }

}
