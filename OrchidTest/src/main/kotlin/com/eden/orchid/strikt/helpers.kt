package com.eden.orchid.strikt

import strikt.api.Assertion
import strikt.api.DescribeableBuilder
import strikt.api.expectThat

fun <T> Assertion.Builder<T>.assertBlock(
    description: String,
    block: Assertion.Builder<T>.(T) -> Any?
): Assertion.Builder<T> {
    var message: Any? = null

    return compose(description) {
        message = block(it)
    }.then {
        when {
            anyFailed -> fail()
            message is AssertBlockFailure -> fail((message as AssertBlockFailure).errorMessage)
            else -> pass()
        }
    }
}

fun <T> Assertion.Builder<T>.assertBlock(
    description: String,
    expected: Any?,
    block: Assertion.Builder<T>.(T) -> Any?
): Assertion.Builder<T> {
    var message: Any? = null

    return compose(description, expected) {
        message = block(it)
    }.then {
        when {
            anyFailed -> fail()
            message is AssertBlockFailure -> fail((message as AssertBlockFailure).errorMessage)
            else -> pass()
        }
    }
}

data class AssertBlockFailure(val errorMessage: String)

fun <T> T.asExpected() : DescribeableBuilder<T> {
    return expectThat(this)
}

fun <T> Assertion.Builder<T>.assertWhen(condition: Boolean, block: Assertion.Builder<T>.()-> Assertion.Builder<T>) : Assertion.Builder<T> {
    if(condition) {
        return block()
    }
    else {
        return this
    }
}
