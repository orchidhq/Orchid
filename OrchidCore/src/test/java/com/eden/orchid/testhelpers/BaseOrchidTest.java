package com.eden.orchid.testhelpers;

import com.caseyjbrooks.clog.Clog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseOrchidTest {

    @BeforeEach
    public void setUp() {
        disableLogging();
    }

    @AfterEach
    public void tearDown() {
        enableLogging();
    }

    protected void disableLogging() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    protected void enableLogging() {
        Clog.getInstance().setMinPriority(Clog.Priority.VERBOSE);
    }

}
