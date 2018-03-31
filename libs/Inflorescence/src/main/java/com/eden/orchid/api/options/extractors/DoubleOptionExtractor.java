package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.DoubleDefault;

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
 * | Field Type | Annotation     | Default Value             |
 * |------------|----------------|---------------------------|
 * | double     | @DoubleDefault | Annotation value() or 0.0 |
 * | Double     | @DoubleDefault | Annotation value() or 0.0 |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class DoubleOptionExtractor extends OptionExtractor<Double> {

    private final DoubleConverter converter;

    @Inject
    public DoubleOptionExtractor(DoubleConverter converter) {
        super(50);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(double.class) || clazz.equals(Double.class);
    }

    @Override
    public Double getOption(Field field, Object sourceObject, String key) {
        return converter.convert(sourceObject).second;
    }

    @Override
    public Double getDefaultValue(Field field) {
        if(field.isAnnotationPresent(DoubleDefault.class)) {
            double[] defaultValue = field.getAnnotation(DoubleDefault.class).value();
            if(defaultValue.length > 0) {
                return defaultValue[0];
            }
        }
        return 0.0;
    }

    @Override
    public String describeDefaultValue(Field field) {
        return Double.toString(getDefaultValue(field));
    }

}
