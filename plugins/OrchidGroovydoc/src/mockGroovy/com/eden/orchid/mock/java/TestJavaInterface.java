package com.eden.orchid.mock.java;

import com.eden.orchid.mock.groovy.TestGroovyClass2;

/**
 * This is the `TestGroovyClass` comment text. It contains <code>code snippets</code>, <b>bold text tags</b>, and
 * also **bold markdown things**.
 */
public interface TestJavaInterface {

    /**
     * This is a field comment
     */
    public String stringField = "";
    public TestGroovyClass2 testGroovyClass2Field = null;

    /**
     * This is a method which returns a string
     *
     * @return the stringField value
     */
    String methodReturningString();

    /**
     * This is a method which returns a string
     *
     * @return the stringField value
     */
    String methodReturningString2();

}
