package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.StringDefault;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class StringOptionExtractor implements OptionExtractor<String> {

    @Override
    public boolean acceptsClass(Class<?> clazz) {
        return clazz.equals(String.class)
                || clazz.equals(String[].class);
    }

    @Override
    public String getOption(Field field, JSONObject options, String key) {
        if(options.has(key) && options.get(key) instanceof String) {
            return options.getString(key);
        }
        else if(field.isAnnotationPresent(StringDefault.class)) {
            return field.getAnnotation(StringDefault.class).value();
        }
        else {
            return null;
        }
    }

    @Override
    public List<String> getList(Field field, JSONObject options, String key) {
        JSONArray array = (options.has(key)) ? options.getJSONArray(key) : new JSONArray();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }

    @Override
    public Object[] getArray(Field field, JSONObject options, String key) {
        List<String> list = this.getList(field, options, key);
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }
}
