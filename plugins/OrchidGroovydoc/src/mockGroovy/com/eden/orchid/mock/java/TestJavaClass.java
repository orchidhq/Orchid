package com.eden.orchid.mock.java;

/**
 * This is the `TestJavaClass` comment text. It contains <code>code snippets</code>, <b>bold text tags</b>, and
 * also **bold markdown things**.
 */
public class TestJavaClass {

    /**
     * This is a field comment
     */
    public transient String stringField = "";
    public transient TestJavaClass2 testJavaClass2Field = null;

    /**
     * This is a method which returns a string
     *
     * @return the stringField value
     */
    public String methodReturningString() {
        return stringField;
    }

    /**
     * This is a method which returns a TestJavaClass2
     *
     * @return the testJavaClass2Field value
     */
    public TestJavaClass2 methodReturningTestJavaClass2() {
        return testJavaClass2Field;
    }

    /**
     * This is the comment text
     *
     * @param a1 the a1 to process
     * @param a2 the a2 to process
     * @param a3 the a3 to process
     * @return the value to return
     */
    public String methodWithParametersReturningString(String a1, int a2, Object a3, TestJavaClass2 a4) {
        return stringField;
    }


}
