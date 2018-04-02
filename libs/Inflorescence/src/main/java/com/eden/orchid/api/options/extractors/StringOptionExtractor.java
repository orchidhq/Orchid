package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.StringDefault;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | String       | @StringDefault | Annotation value() or null |
 * | String[]     | none           | Empty String[]             |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class StringOptionExtractor extends OptionExtractor<String> {

    private final StringConverter converter;

    @Inject
    public StringOptionExtractor(StringConverter converter) {
        super(10);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(String.class);
    }

    @Override
    public String getOption(Field field, Object sourceObject, String key) {
        return converter.convert(sourceObject).second;
    }

    @Override
    public String getDefaultValue(Field field) {
        if(field.isAnnotationPresent(StringDefault.class)) {
            String[] defaultValue = field.getAnnotation(StringDefault.class).value();
            if(defaultValue.length > 0) {
                return defaultValue[0];
            }
        }
        return "";
    }

    @Override
    public String describeDefaultValue(Field field) {
        String value = getDefaultValue(field);

        if(!EdenUtils.isEmpty(value)) {
            return value;
        }
        else {
            return "empty string";
        }
    }

}
