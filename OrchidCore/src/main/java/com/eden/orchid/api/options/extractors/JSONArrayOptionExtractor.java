package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.options.OptionExtractor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

public class JSONArrayOptionExtractor implements OptionExtractor<JSONArray> {

    @Override
    public boolean acceptsClass(Class<?> clazz) {
        return clazz.equals(JSONArray.class);
    }

    @Override
    public JSONArray getOption(Field field, JSONObject options, String key) {
        if(options.has(key) && options.get(key) instanceof JSONArray) {
            return options.getJSONArray(key);
        }
        else {
            return null;
        }
    }

    @Override public List<JSONArray> getList(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting List<JSONArray> not supported");
    }

    @Override public Object getArray(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting JSONArray[] not supported");
    }
}
