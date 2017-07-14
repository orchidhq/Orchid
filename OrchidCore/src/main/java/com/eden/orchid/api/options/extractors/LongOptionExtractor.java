package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.LongDefault;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LongOptionExtractor implements OptionExtractor<Long> {

    @Override
    public boolean acceptsClass(Class<?> clazz) {
        return clazz.equals(long.class)
                || clazz.equals(Long.class)
                || clazz.equals(long[].class)
                || clazz.equals(Long[].class);
    }

    @Override
    public Long getOption(Field field, JSONObject options, String key) {
        if(options.has(key) && options.get(key) instanceof Long) {
            return options.getLong(key);
        }
        else if(field.isAnnotationPresent(LongDefault.class)) {
            return field.getAnnotation(LongDefault.class).value();
        }
        else {
            return 0L;
        }
    }

    @Override
    public List<Long> getList(Field field, JSONObject options, String key) {
        JSONArray array = (options.has(key)) ? options.getJSONArray(key) : new JSONArray();
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getLong(i));
        }
        return list;
    }

    @Override
    public Object getArray(Field field, JSONObject options, String key) {
        List<Long> list = this.getList(field, options, key);

        if (field.getType().equals(long[].class)) {
            long[] array = new long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }
            return array;
        }
        else if (field.getType().equals(Long[].class)) {
            Long[] array = new Long[list.size()];
            list.toArray(array);
            return array;
        }

        return null;
    }
}
