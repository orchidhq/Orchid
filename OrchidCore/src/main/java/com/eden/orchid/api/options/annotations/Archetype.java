package com.eden.orchid.api.options.annotations;

import com.eden.orchid.api.options.OptionArchetype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Archetypes.class)
public @interface Archetype {
    String key();
    Class<? extends OptionArchetype> value();
}
