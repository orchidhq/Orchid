package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.options.OptionExtractor;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
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
 */
public final class JSONArrayOptionExtractor extends OptionExtractor<JSONArray> {

    @Inject
    public JSONArrayOptionExtractor() {
        super(10);
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(JSONArray.class);
    }

    @Override
    public JSONArray getOption(Field field, JSONObject options, String key) {
        if(options.has(key) && options.get(key) instanceof JSONArray) {
            return options.getJSONArray(key);
        }
        else {
            return getDefaultValue(field);
        }
    }

    @Override
    public JSONArray getDefaultValue(Field field) {
        return new JSONArray();
    }

    @Override public List<JSONArray> getList(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting List<JSONArray> not supported");
    }

    @Override public Object getArray(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting JSONArray[] not supported");
    }
}
