package com.eden.orchid.api.theme.assets;

import com.caseyjbrooks.clog.Clog;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups={"assets", "unit"})
public class TestAssets {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    @Test
    public void testMethod() throws Throwable {

    }

}
