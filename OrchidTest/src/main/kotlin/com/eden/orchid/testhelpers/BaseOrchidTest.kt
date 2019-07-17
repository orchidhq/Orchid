package com.eden.orchid.testhelpers

import com.caseyjbrooks.clog.Clog
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

interface BaseOrchidTest {

    @BeforeEach
    @JvmDefault
    fun baseSetUp() {
        disableLogging()
    }

    @AfterEach
    @JvmDefault
    fun baseTearDown() {
        enableLogging()
    }

    @JvmDefault
    fun disableLogging() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL)
    }

    @JvmDefault
    fun enableLogging() {
        Clog.getInstance().setMinPriority(Clog.Priority.VERBOSE)
    }

}
