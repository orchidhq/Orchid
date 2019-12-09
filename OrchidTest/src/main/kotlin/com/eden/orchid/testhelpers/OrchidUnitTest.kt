package com.eden.orchid.testhelpers

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

interface OrchidUnitTest : BaseOrchidTest {

    @BeforeEach
    @JvmDefault
    fun unitTestSetUp() {

    }

    @AfterEach
    @JvmDefault
    fun unitTestTearDown() {

    }

}
