package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.options.OptionExtractor;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

/**
 * ### Source Types
 *
 * | Item Type  | Coercion |
 * |------------|----------|
 * | JSONObject | direct   |
 * | Map        | new JSONObject from map |
 *
 *
 * ### Destination Types
 *
 * | Field Type | Annotation  | Default Value |
 * |------------|-------------|---------------|
 * | JSONObject | none        | null          |
 */
public class JSONObjectOptionExtractor implements OptionExtractor<JSONObject> {

    @Override
    public boolean acceptsClass(Class<?> clazz) {
        return clazz.equals(JSONObject.class);
    }

    @Override
    public JSONObject getOption(Field field, JSONObject options, String key) {
        if(options.has(key) && options.get(key) instanceof JSONObject) {
            return options.getJSONObject(key);
        }
        else {
            return null;
        }
    }

    @Override public List<JSONObject> getList(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting List<JSONObject> not supported, try JSONArray instead");
    }

    @Override public Object getArray(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting JSONObject[] not supported, try JSONArray instead");
    }
}
