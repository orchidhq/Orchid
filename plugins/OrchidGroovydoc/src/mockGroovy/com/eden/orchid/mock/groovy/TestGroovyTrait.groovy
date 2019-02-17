package com.eden.orchid.mock.groovy

/**
 * This is the `TestGroovyClass` comment text. It contains <code>code snippets</code>, <b>bold text tags</b>, and
 * also **bold markdown things**.
 */
trait TestGroovyTrait {

    /**
     * This is a field comment
     */
    public String stringField = ""
    public TestGroovyClass2 testGroovyClass2Field = null

    /**
     * This is a method which returns a string
     *
     * @return the stringField value
     */
    String methodReturningString() {
        return ""
    }

    /**
     * This is a method which returns a string
     *
     * @return the stringField value
     */
    String methodReturningString2() {
        return ""
    }

}
