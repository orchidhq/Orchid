package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.ListClass;
import com.eden.orchid.api.options.converters.Converters;
import com.eden.orchid.api.options.converters.FlexibleIterableConverter;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
public final class ListOptionExtractor extends OptionExtractor<List> {

    private final FlexibleIterableConverter converter;
    private final Converters converters;

    @Inject
    public ListOptionExtractor(FlexibleIterableConverter converter, Converters converters) {
        super(10);
        this.converter = converter;
        this.converters = converters;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    public List getOption(Field field, Object sourceObject, String key) {
        EdenPair<Boolean, Iterable> value = converter.convert(sourceObject);

        List<Object> list = new ArrayList<>();

        Class<?> listClass = (field.isAnnotationPresent(ListClass.class))
                ? field.getAnnotation(ListClass.class).value()
                : Object.class;

        for(Object item : value.second) {
            EdenPair<Boolean, ?> converted = converters.convert(item, listClass);

            if(converted.first) {
                list.add(converted.second);
            }
        }

        return list;
    }

    @Override
    public List getDefaultValue(Field field) {
        return new ArrayList();
    }

}
