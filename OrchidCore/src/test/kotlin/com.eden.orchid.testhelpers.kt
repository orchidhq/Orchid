package com.eden.orchid.testhelpers

import strikt.api.Assertion

fun Assertion.Builder<TestResults>.pageWasRendered(name: String) =
        assert("page was rendered at $name") {
            if(subject.renderedPageMap[name] != null) pass()
            else fail()
        }

fun Assertion.Builder<TestResults>.pageWasNotRendered(name: String) =
        assert("page was rendered at $name") {
            if(subject.renderedPageMap[name] != null) fail()
            else pass()
        }