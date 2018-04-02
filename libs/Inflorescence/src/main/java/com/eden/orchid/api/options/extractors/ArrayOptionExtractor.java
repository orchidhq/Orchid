package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.options.converters.Converters;
import com.eden.orchid.api.options.converters.FlexibleIterableConverter;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ### Source Types
 *
 * | Item Type  | Coercion |
 * |------------|----------|
 * | JSONArray  | direct   |
 * | anything[] | new JSONArray from array |
 * | List       | new JSONArray from list |
 *
 *
 * ### Destination Types
 *
 * | Field Type | Annotation  | Default Value |
 * |------------|-------------|---------------|
 * | JSONArray  | none        | null          |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class ArrayOptionExtractor extends OptionExtractor<String[]> {

    private final FlexibleIterableConverter converter;
    private final Converters converters;

    @Inject
    public ArrayOptionExtractor(FlexibleIterableConverter converter, Converters converters) {
        super(2);
        this.converter = converter;
        this.converters = converters;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return String[].class.equals(clazz);
    }

    @Override
    public String[] getOption(Field field, Object sourceObject, String key) {
        EdenPair<Boolean, Iterable> value = converter.convert(sourceObject);

        List<Object> list = new ArrayList<>();

        Class<?> arrayClass = field.getType().getComponentType();

        for(Object item : value.second) {
            EdenPair<Boolean, ?> converted = converters.convert(item, arrayClass);

            if(converted.first) {
                list.add(converted.second);
            }
        }


        String[] array = new String[list.size()];
        list.toArray(array);
        return array;

    }

    @Override
    public String[] getDefaultValue(Field field) {
        if(field.isAnnotationPresent(StringDefault.class)) {
            return field.getAnnotation(StringDefault.class).value();
        }

        return new String[0];
    }

    @Override
    public String describeDefaultValue(Field field) {
        String[] value = getDefaultValue(field);

        if(value.length > 0) {
            return "[" + Arrays.stream(value)
                    .map(item -> converters.convert(item, String.class).second)
                    .collect(Collectors.joining(", ")) + "]";
        }

        return "empty array";
    }

}
