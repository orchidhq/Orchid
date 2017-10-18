package com.eden.orchid.api.options.annotations;

import com.eden.orchid.api.options.OptionValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validate {
    String[] value() default {};
    Class<? extends OptionValidator>[] rules() default {};
    boolean throwIfInvalid() default false;

}
