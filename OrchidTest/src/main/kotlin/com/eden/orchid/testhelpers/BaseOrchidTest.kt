package com.eden.orchid.testhelpers

import com.caseyjbrooks.clog.Clog
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

open class BaseOrchidTest {

    @BeforeEach
    fun baseSetUp() {
        disableLogging()
    }

    @AfterEach
    fun baseTearDown() {
        enableLogging()
    }

    protected fun disableLogging() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL)
    }

    protected fun enableLogging() {
        Clog.getInstance().setMinPriority(Clog.Priority.VERBOSE)
    }

}
