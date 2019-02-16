package com.eden.orchid.mock.groovy

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
public @interface TestGroovyAnnotation {

    int anInt() default 0

}
