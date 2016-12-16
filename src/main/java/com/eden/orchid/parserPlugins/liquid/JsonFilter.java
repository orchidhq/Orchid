package com.eden.orchid.parserPlugins.liquid;

import com.eden.orchid.AutoRegister;
import liqp.filters.Filter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

@AutoRegister
public class JsonFilter extends Filter {

    public JsonFilter() {
        super("json");
    }

    @Override
    public Object apply(Object value, Object... params) {

        if(value instanceof JSONObject) {
            return ((JSONObject) value).toString(2);
        }
        else if(value instanceof JSONArray) {
            return ((JSONArray) value).toString(2);
        }
        else if(value instanceof Map) {
            return new JSONObject((Map) value).toString(2);
        }
        else if(value instanceof Collection) {
            return new JSONArray((Collection) value).toString(2);
        }

        return value;
    }
}
