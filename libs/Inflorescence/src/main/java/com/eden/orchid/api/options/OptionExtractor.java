package com.eden.orchid.api.options;

import com.eden.orchid.api.registration.Prioritized;

import java.lang.reflect.Field;

/**
 * Extract classes of a specific type from a JSON-like structure.
 *
 * @since v1.0.0
 * @orchidApi extensible
 * @param <T> the type of object that this Extractor will return
 */
public abstract class OptionExtractor<T> extends Prioritized {

    public OptionExtractor(int priority) {
        super(priority);
    }

    public abstract boolean acceptsClass(Class clazz);

    public abstract T getOption(Field field, Object sourceObject, String key);

    public boolean isEmptyValue(T value) {
        return value == null;
    }

    public T getDefaultValue(Field field) {
        return null;
    }

    public String describeDefaultValue(Field field) {
        T value = getDefaultValue(field);

        if(value == null) {
            return "null";
        }
        else {
            return value.toString();
        }
    }

}
