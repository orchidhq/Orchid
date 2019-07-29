package com.eden.orchid.mock

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * This is the text for a Groovy Annotation class.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GroovyAnnotation {

    /**
     * Set the value of `anInt`. Default is 0
     *
     * @return the value of `anInt`
     */
    int anInt() default 0

}
