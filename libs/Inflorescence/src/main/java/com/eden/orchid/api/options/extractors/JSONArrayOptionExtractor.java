package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.converters.FlexibleIterableConverter;
import org.json.JSONArray;

import javax.inject.Inject;
import java.lang.reflect.Field;

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
public final class JSONArrayOptionExtractor extends OptionExtractor<JSONArray> {

    private final FlexibleIterableConverter converter;

    @Inject
    public JSONArrayOptionExtractor(FlexibleIterableConverter converter) {
        super(10);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(JSONArray.class);
    }

    @Override
    public JSONArray getOption(Field field, Object sourceObject, String key) {
        EdenPair<Boolean, Iterable> value = converter.convert(sourceObject);

        JSONArray jsonArray = new JSONArray();

        for(Object item : value.second) {
            jsonArray.put(item);
        }

        return jsonArray;
    }

    @Override
    public JSONArray getDefaultValue(Field field) {
        return new JSONArray();
    }

}
