package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.converters.Converters;
import com.eden.orchid.api.options.converters.FlexibleMapConverter;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

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
public final class MapOptionExtractor extends OptionExtractor<Map> {

    private final FlexibleMapConverter converter;
    private final Converters converters;

    @Inject
    public MapOptionExtractor(FlexibleMapConverter converter, Converters converters) {
        super(10);
        this.converter = converter;
        this.converters = converters;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public Map getOption(Field field, Object sourceObject, String key) {
        EdenPair<Boolean, Map> value = converter.convert(sourceObject);

        Map<String, Object> map = new HashMap<>();

        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        Class<?> listClass = (Class<?>) stringListType.getActualTypeArguments()[1];

        for(Map.Entry<Object, Object> item : ((Map<Object, Object>) value.second).entrySet()) {
            EdenPair<Boolean, String> itemKey = converters.convert(item, String.class);
            EdenPair<Boolean, ?> converted    = converters.convert(item, listClass);

            if(converted.first) {
                map.put(itemKey.second, converted.second);
            }
        }

        return map;
    }

    @Override
    public Map getDefaultValue(Field field) {
        return new HashMap();
    }

}
