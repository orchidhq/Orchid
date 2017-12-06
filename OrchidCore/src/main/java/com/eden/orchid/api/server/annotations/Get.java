package com.eden.orchid.api.server.annotations;

import com.eden.orchid.api.options.OptionsHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Get {
    String path();
    Class<? extends OptionsHolder> params() default OptionsHolder.class;
}
