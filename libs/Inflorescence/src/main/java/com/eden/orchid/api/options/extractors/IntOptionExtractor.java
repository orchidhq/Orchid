package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.IntegerConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.IntDefault;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * ### Source Types
 *
 * | Item Type | Coercion |
 * |-----------|----------|
 * | number    | direct   |
 * | string    | parse as number |
 *
 *
 * ### Destination Types
 *
 * | Field Type | Annotation  | Default Value           |
 * |------------|-------------|-------------------------|
 * | int        | @IntDefault | Annotation value() or 0 |
 * | Integer    | @IntDefault | Annotation value() or 0 |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class IntOptionExtractor extends OptionExtractor<Integer> {

    private final IntegerConverter converter;

    @Inject
    public IntOptionExtractor(IntegerConverter converter) {
        super(50);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(int.class) || clazz.equals(Integer.class);
    }

    @Override
    public Integer getOption(Field field, Object sourceObject, String key) {
        return converter.convert(sourceObject).second;
    }

    @Override
    public Integer getDefaultValue(Field field) {
        if(field.isAnnotationPresent(IntDefault.class)) {
            int[] defaultValue = field.getAnnotation(IntDefault.class).value();
            if(defaultValue.length > 0) {
                return defaultValue[0];
            }
        }
        return 0;
    }

    @Override
    public String describeDefaultValue(Field field) {
        return Integer.toString(getDefaultValue(field));
    }

}
