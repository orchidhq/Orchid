package com.eden.orchid.mock

/**
 * This is the comment text for a trait defined in **Groovy**.
 */
trait GroovyTrait {

    /**
     * Field comment for stringField of type {@link String}
     */
    public String stringField = ""

    /**
     * Field comment for groovyClassField of type {@link GroovyClass}
     */
    public GroovyClass groovyClassField = null

    /**
     * Method comment for methodReturningString which returns {@link String}
     *
     * @return a String
     */
    String methodReturningString() {
        return stringField
    }

    /**
     * Method comment for methodReturningGroovyClass which returns {@link String}
     *
     * @return a GroovyClass
     */
    GroovyClass methodReturningGroovyClass() {
        return groovyClassField
    }
}
