package com.eden.orchid.testhelpers;

import com.caseyjbrooks.clog.Clog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class OrchidIntegrationTest {

    @BeforeEach
    void setUp() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    @AfterEach
    void tearDown() {
        Clog.getInstance().setMinPriority(Clog.Priority.VERBOSE);
    }

}
