package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.BooleanConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.BooleanDefault;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * ### Destination Types
 *
 * | Field Type | Annotation      | Default Value               |
 * |------------|-----------------|-----------------------------|
 * | boolean    | @BooleanDefault | Annotation value() or false |
 * | Boolean    | @BooleanDefault | Annotation value() or false |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class BooleanOptionExtractor extends OptionExtractor<Boolean> {

    private final BooleanConverter converter;

    @Inject
    public BooleanOptionExtractor(BooleanConverter converter) {
        super(50);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(boolean.class) || clazz.equals(Boolean.class);
    }

    @Override
    public Boolean getOption(Field field, Object sourceObject, String key) {
        return converter.convert(sourceObject).second;
    }

    @Override
    public Boolean getDefaultValue(Field field) {
        if(field.isAnnotationPresent(BooleanDefault.class)) {
            boolean[] defaultValue = field.getAnnotation(BooleanDefault.class).value();
            if(defaultValue.length > 0) {
                return defaultValue[0];
            }
        }
        return false;
    }

    @Override
    public String describeDefaultValue(Field field) {
        return Boolean.toString(getDefaultValue(field));
    }

}
