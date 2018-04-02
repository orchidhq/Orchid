package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.FloatConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.FloatDefault;

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
 * | Field Type | Annotation    | Default Value              |
 * |------------|---------------|----------------------------|
 * | float      | @FloatDefault | Annotation value() or 0.0f |
 * | Float      | @FloatDefault | Annotation value() or 0.0f |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class FloatOptionExtractor extends OptionExtractor<Float> {

    private final FloatConverter converter;

    @Inject
    public FloatOptionExtractor(FloatConverter converter) {
        super(50);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(float.class) || clazz.equals(Float.class);
    }

    @Override
    public Float getOption(Field field, Object sourceObject, String key) {
        return converter.convert(sourceObject).second;
    }

    @Override
    public Float getDefaultValue(Field field) {
        if(field.isAnnotationPresent(FloatDefault.class)) {
            float[] defaultValue = field.getAnnotation(FloatDefault.class).value();
            if(defaultValue.length > 0) {
                return defaultValue[0];
            }
        }
        return 0.0f;
    }

    @Override
    public String describeDefaultValue(Field field) {
        return Float.toString(getDefaultValue(field));
    }

}
