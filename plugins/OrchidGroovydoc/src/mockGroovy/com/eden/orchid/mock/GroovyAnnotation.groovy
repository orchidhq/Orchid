package com.eden.orchid.mock

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
public @interface GroovyAnnotation {

    int anInt() default 0

}
