package com.eden.orchid.testhelpers

import strikt.api.Assertion

fun Assertion.Builder<TestResults>.somethingRendered() =
        assert("some pages were rendered") {
            if(it.renderedPageMap.isEmpty()) fail()
            else pass()
        }

fun Assertion.Builder<TestResults>.nothingRendered() =
        assert("no pages were rendered") {
            if(it.renderedPageMap.isEmpty()) pass()
            else fail()
        }

fun Assertion.Builder<TestResults>.pageWasRendered(name: String) =
        assert("page was rendered at $name") {
            if(it.renderedPageMap[name] != null) pass()
            else fail()
        }

fun Assertion.Builder<TestResults>.pageWasNotRendered(name: String) =
        assert("page was rendered at $name") {
            if(it.renderedPageMap[name] != null) fail()
            else pass()
        }