package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.LongDefault;

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
 * | Field Type | Annotation   | Default Value            |
 * |------------|--------------|--------------------------|
 * | long       | @LongDefault | Annotation value() or 0L |
 * | Long       | @LongDefault | Annotation value() or 0L |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class LongOptionExtractor extends OptionExtractor<Long> {

    private final LongConverter converter;

    @Inject
    public LongOptionExtractor(LongConverter converter) {
        super(50);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(long.class) || clazz.equals(Long.class);
    }

    @Override
    public Long getOption(Field field, Object sourceObject, String key) {
        return converter.convert(sourceObject).second;
    }

    @Override
    public Long getDefaultValue(Field field) {
        if(field.isAnnotationPresent(LongDefault.class)) {
            long[] defaultValue = field.getAnnotation(LongDefault.class).value();
            if(defaultValue.length > 0) {
                return defaultValue[0];
            }
        }
        return 0L;
    }

    @Override
    public String describeDefaultValue(Field field) {
        return Long.toString(getDefaultValue(field));
    }

}
