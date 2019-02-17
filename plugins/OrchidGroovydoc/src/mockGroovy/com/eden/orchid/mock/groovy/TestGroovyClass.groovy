package com.eden.orchid.mock.groovy

/**
 * This is the `TestGroovyClass` comment text. It contains <code>code snippets</code>, <b>bold text tags</b>, and
 * also **bold markdown things**.
 */
public class TestGroovyClass {

    /**
     * This is a field comment
     */
    public transient String stringField = ""
    public transient TestGroovyClass2 testGroovyClass2Field = null

    /**
     * This is a method which returns a string
     *
     * @return the stringField value
     */
    String methodReturningString() {
        return stringField
    }

    /**
     * This is a method which returns a TestGroovyClass2
     *
     * @return the testGroovyClass2Field value
     */
    TestGroovyClass2 methodReturningTestGroovyClass2() {
        return testGroovyClass2Field
    }

    /**
     * This is the comment text
     *
     * @param a1 the a1 to process
     * @param a2 the a2 to process
     * @param a3 the a3 to process
     * @return the value to return
     */
    String methodWithParametersReturningString(String a1, int a2, Object a3, TestGroovyClass2 a4) {
        return stringField
    }


}
