package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.options.OptionExtractor;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class AnyOptionExtractor extends OptionExtractor<Object> {

    @Inject
    public AnyOptionExtractor() {
        super(1);
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return true;
    }

    @Override
    public Object getOption(Field field, Object sourceObject, String key) {
        return sourceObject;
    }

}
