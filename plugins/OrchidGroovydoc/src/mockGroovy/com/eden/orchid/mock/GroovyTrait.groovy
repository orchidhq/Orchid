package com.eden.orchid.mock

trait GroovyTrait {

    public String stringField = ""
    public GroovyClass groovyClassField = null

    String methodReturningString() {
        return stringField
    }

    GroovyClass methodReturningGroovyClass() {
        return groovyClassField
    }
}
