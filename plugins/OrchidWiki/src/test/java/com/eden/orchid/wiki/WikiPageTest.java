package com.eden.orchid.wiki;

import com.caseyjbrooks.clog.Clog;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups = {"options", "unit"})
public class WikiPageTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private WikiTestableObject testableObject;

    @BeforeMethod
    public void setupTest() {
        testableObject = new WikiTestableObject();
    }

    @Test
    public void testWikiPage() {
        testableObject.setOrder(100);
        assertThat(testableObject.getOrder(), is(equalTo(100)));
    }

}