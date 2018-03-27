package com.eden.orchid.api.theme.menus;

import com.caseyjbrooks.clog.Clog;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups={"menus", "unit"})
public class TestOrchidMenu {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    @Test
    public void testMethod() throws Throwable {

    }

}
