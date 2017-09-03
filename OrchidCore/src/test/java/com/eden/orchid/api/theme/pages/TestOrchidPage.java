package com.eden.orchid.api.theme.pages;

import com.caseyjbrooks.clog.Clog;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups={"pages", "unit"})
public class TestOrchidPage {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    @Test
    public void testMethod() throws Throwable {

    }

}
