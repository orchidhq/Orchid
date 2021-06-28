package com.eden.orchid.testhelpers

import clog.Clog
import clog.dsl.setMinPriority
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
        Clog.setMinPriority(Clog.Priority.FATAL)
    }

    @JvmDefault
    fun enableLogging() {
        Clog.setMinPriority(Clog.Priority.VERBOSE)
    }

}
